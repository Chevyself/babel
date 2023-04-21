package com.github.chevyself.babel.api.tab;

import chevyself.github.commands.util.Pair;
import com.github.chevyself.babel.api.tab.entries.EmptyTabEntry;
import com.github.chevyself.babel.api.tab.entries.TabEntry;
import com.github.chevyself.babel.debug.ErrorHandler;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.craft.WrappedCraftPlayer;
import com.github.chevyself.babel.packet.entity.player.WrappedEntityPlayer;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfo;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfoAction;
import com.github.chevyself.reflect.modifiers.CollectionModifier;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This class represents a tab view for a player. This is the implementation of {@link TabView} for
 * players, and it does send the packets to change the client.
 *
 * <p>This class is thread safe.
 */
@Getter
public class PlayerTabView implements TabView {

  @NonNull private final UUID viewer;
  @NonNull private final List<TabSlot> slots;
  @NonNull private final TabSize size;
  private boolean destroyed;

  /**
   * Create the tab view.
   *
   * @param viewer the uuid of the viewer of the tab view
   * @param size the size of the tab view
   * @throws NullPointerException if the viewer or size is null
   */
  public PlayerTabView(@NonNull UUID viewer, @NonNull TabSize size) {
    this.viewer = viewer;
    this.slots = new ArrayList<>();
    this.size = size;
    this.destroyed = false;
  }

  /**
   * Converts the slots into a list of {@link WrappedPlayerInfo} to send to the client.
   *
   * @param slots the slots to convert
   * @param player the player that will view the entries
   * @param packet the packet that will send the player info
   * @return the list of player info
   * @throws NullPointerException if any argument is null
   */
  private static List<WrappedPlayerInfo> collectSlotsPlayerInfo(
      @NonNull Collection<TabSlot> slots, @NonNull Player player, @NonNull Packet packet) {
    return slots.stream()
        .map(
            slot -> {
              try {
                return slot.playerInfoData(player, packet);
              } catch (PacketHandlingException e) {
                ErrorHandler.getInstance()
                    .handle(Level.SEVERE, "Could not get PlayerInfoData for slot " + slot);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Get the unique id of the viewer.
   *
   * @return the unique id of the viewer
   */
  @NonNull
  public UUID getUniqueId() {
    return viewer;
  }

  /**
   * Get the viewers' entity. This means the actual instance of the player.
   *
   * @return the entity wrapped in an optional
   */
  @NonNull
  public Optional<Player> getViewer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.viewer));
  }

  @Override
  public void clear() {
    this.getViewer()
        .ifPresent(
            player -> {
              try {
                Packet packet = this.createPacket();
                List<WrappedPlayerInfo> wrappers = this.collectSlotsPlayerInfo(player, packet);
                packet.setField(0, WrappedPlayerInfoAction.REMOVE_PLAYER);
                packet.setField(1, CollectionModifier.addWrappers(wrappers));
                packet.send(player);
              } catch (PacketHandlingException e) {
                ErrorHandler.getInstance()
                    .handle(Level.SEVERE, "Could not clear tab view for " + player.getName());
              }
            });
  }

  private Packet createPacket() throws PacketHandlingException {
    return PacketType.Play.ClientBound.PLAYER_INFO.create(
        WrappedPlayerInfoAction.ADD_PLAYER.getWrapped(), WrappedEntityPlayer.createArray(0));
  }

  /**
   * Converts the slots into a list of {@link WrappedPlayerInfo} to send to the client.
   *
   * @see #collectSlotsPlayerInfo(Collection, Player, Packet)
   * @param player the player that will view the entries
   * @param packet the packet that will send the player info
   * @return the list of player info
   * @throws NullPointerException if any argument is null
   */
  @NonNull
  private List<WrappedPlayerInfo> collectSlotsPlayerInfo(
      @NonNull Player player, @NonNull Packet packet) {
    return PlayerTabView.collectSlotsPlayerInfo(slots, player, packet);
  }

  @Override
  public void initialize() throws PacketHandlingException {
    if (destroyed || !slots.isEmpty()) return;
    Optional<Player> optional = this.getViewer();
    if (optional.isPresent()) {
      Player player = optional.get();
      Packet packet = this.createPacket();
      List<WrappedPlayerInfo> info = this.populate(packet, player);
      packet.setField(1, CollectionModifier.addWrappers(info));
      packet.send(player);
    } else {
      destroyed = true;
    }
  }

  /**
   * Fills the slots with empty entries.
   *
   * @param packet the packet that will send the player info
   * @param player the player that will view the entries
   * @return the list of player info from the slots
   * @throws PacketHandlingException if the packet could not be handled
   */
  @NonNull
  private List<WrappedPlayerInfo> populate(@NonNull Packet packet, @NonNull Player player)
      throws PacketHandlingException {
    List<WrappedPlayerInfo> info = new ArrayList<>();
    for (TabCoordinate coordinate : size) {
      TabSlot slot = new TabSlot(coordinate, new EmptyTabEntry());
      info.add(slot.playerInfoData(player, packet));
      this.slots.add(slot);
    }
    Collections.sort(this.slots);
    return info;
  }

  /**
   * Get the online players as {@link WrappedPlayerInfo}. This is used to send the information of
   * actual players to the client.
   *
   * @deprecated This may no longer be needed as it is safer to push the players out of the tab list
   * @param packet the packet that will send the player info
   * @return the wrapped player info
   * @throws NullPointerException if the packet is null
   */
  private @NonNull List<WrappedPlayerInfo> getPlayers(@NonNull Packet packet) {
    return Bukkit.getOnlinePlayers().stream()
        .map(WrappedCraftPlayer::of)
        .map(
            craftPlayer -> {
              try {
                return craftPlayer.getHandle().playerInfo(packet);
              } catch (PacketHandlingException e) {
                ErrorHandler.getInstance()
                    .handle(Level.SEVERE, "Could not get entity from CraftPlayer", e);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry) {
    TabSlot slot = this.getSlot(coordinate);
    this.set(slot, entry, this.updatesSkin(slot, entry));
  }

  /**
   * Set the entry of a slot.
   *
   * @param slot the slot to set
   * @param entry the entry to set
   * @param skin whether the skin of the slot should be updated
   * @throws NullPointerException if slot or entry is null
   */
  public void set(@NonNull TabSlot slot, @NonNull TabEntry entry, boolean skin) {
    slot.setEntry(entry);
    this.update(Collections.singleton(slot), skin);
  }

  /**
   * Update the slots view in the client.
   *
   * @param slots the slots to update
   * @param skin whether the skin of the slots should be updated
   */
  private void update(@NonNull Collection<TabSlot> slots, boolean skin) {
    this.getViewer()
        .ifPresent(
            player -> {
              try {
                Packet packet = this.createPacket();
                packet.setField(
                    1,
                    CollectionModifier.addWrappers(
                        PlayerTabView.collectSlotsPlayerInfo(slots, player, packet)));
                if (skin) {
                  packet.setField(0, WrappedPlayerInfoAction.REMOVE_PLAYER);
                  packet.send(player);
                  packet = this.createPacket();
                  packet.setField(
                      1,
                      CollectionModifier.addWrappers(
                          PlayerTabView.collectSlotsPlayerInfo(slots, player, packet)));
                  packet.setField(0, WrappedPlayerInfoAction.ADD_PLAYER);
                  packet.send(player);
                } else {
                  packet.setField(0, WrappedPlayerInfoAction.UPDATE_DISPLAY_NAME);
                  packet.send(player);
                }
              } catch (PacketHandlingException e) {
                ErrorHandler.getInstance().handle(Level.SEVERE, "Could not update tab slot", e);
              }
            });
  }

  @NonNull
  private TabSlot getSlot(@NonNull TabCoordinate coordinate) {
    return this.slots.stream()
        .filter(slot -> slot.getCoordinate().equals(coordinate))
        .findFirst()
        .orElseThrow(
            () ->
                new IndexOutOfBoundsException("Could not find slot with coordinate " + coordinate));
  }

  @Override
  public boolean add(@NonNull TabEntry entry) {
    return this.add(Collections.singleton(entry));
  }

  @Override
  public boolean add(@NonNull Collection<TabEntry> entries) {
    Map<TabSlot, TabEntry> toUpdate = new HashMap<>();
    Pair<List<TabSlot>, List<TabEntry>> pair = this.getReplacements(entries);
    List<TabSlot> canBeReplaced = pair.getA();
    List<TabEntry> allEntries = pair.getB();
    for (int i = 0; i < canBeReplaced.size(); i++) {
      if (allEntries.size() > i) {
        if (!canBeReplaced.get(i).getEntry().equals(allEntries.get(i))) {
          toUpdate.put(canBeReplaced.get(i), allEntries.get(i));
        }
      } else {
        break;
      }
    }
    this.set(toUpdate);
    return toUpdate.size() > 0;
  }

  @Override
  public boolean remove(
      @NonNull Supplier<TabEntry> replacement, @NonNull Collection<? extends TabEntry> entries) {
    Map<TabSlot, TabEntry> toUpdate = new HashMap<>();
    slots.stream()
        .filter(
            slot -> {
              for (TabEntry entry : entries) {
                if (entry.equals(slot.getEntry())) {
                  return true;
                }
              }
              return false;
            })
        .forEach(slot -> toUpdate.put(slot, replacement.get()));
    this.set(toUpdate);
    return toUpdate.size() > 0;
  }

  @Override
  public void sort() {
    Map<TabSlot, TabEntry> toUpdate = new HashMap<>();
    List<TabEntry> sortedEntries =
        this.slots.stream().map(TabSlot::getEntry).sorted().collect(Collectors.toList());
    for (int i = 0; i < slots.size(); i++) {
      TabSlot tabSlot = slots.get(i);
      TabEntry tabEntry = sortedEntries.get(i);
      if (!tabSlot.getEntry().equals(tabEntry)) {
        toUpdate.put(tabSlot, tabEntry);
      }
    }
    this.set(toUpdate);
  }

  @NonNull
  private Pair<List<TabSlot>, List<TabEntry>> getReplacements(
      @NonNull Collection<TabEntry> entries) {
    return this.getReplacements(entries, (slot, entry) -> slot.getEntry().canBeReplaced(entry));
  }

  /**
   * Get the slots from which the current entry can be replaced by any of the entries.
   *
   * @param entries the entries to replace
   * @param predicate the predicate to check if a slot can be replaced by an entry
   * @return the slots that can be replaced by the entries
   */
  @NonNull
  private Pair<List<TabSlot>, List<TabEntry>> getReplacements(
      @NonNull Collection<? extends TabEntry> entries,
      @NonNull BiPredicate<TabSlot, TabEntry> predicate) {
    List<TabSlot> canBeReplaced = new ArrayList<>();
    List<TabEntry> allEntries = new ArrayList<>(entries);
    entries.forEach(
        entry ->
            this.slots.stream()
                .filter(slot -> predicate.test(slot, entry) && !canBeReplaced.contains(slot))
                .forEach(
                    slot -> {
                      canBeReplaced.add(slot);
                      allEntries.add(slot.getEntry());
                    }));
    Collections.sort(canBeReplaced);
    Collections.sort(allEntries);
    return new Pair<>(canBeReplaced, allEntries);
  }

  private void set(@NonNull Map<TabSlot, TabEntry> toUpdate) {
    List<TabSlot> update = new ArrayList<>();
    List<TabSlot> updateSkin = new ArrayList<>();
    toUpdate.forEach(
        (slot, entry) -> {
          boolean skin = this.updatesSkin(slot, entry);
          slot.setEntry(entry);
          if (skin) {
            updateSkin.add(slot);
          } else {
            update.add(slot);
          }
        });
    this.update(update, false);
    this.update(updateSkin, true);
  }

  /**
   * Checks whether the skin of the slot should be updated with a new entry.
   *
   * @param slot the slot to check
   * @param entry the entry to check
   * @return true if the new entry replaces the skin
   * @throws NullPointerException if the slot or entry is null
   */
  private boolean updatesSkin(@NonNull TabSlot slot, @NonNull TabEntry entry) {
    return slot.getEntry().getSkin() == null && entry.getSkin() != null
        || !slot.getEntry().getSkin().equals(entry.getSkin());
  }
}
