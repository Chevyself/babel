package me.googas.chat.api.tab;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.api.tab.entries.EmptyTabEntry;
import me.googas.chat.api.tab.entries.TabEntry;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.entity.player.WrappedCraftPlayer;
import me.googas.chat.packet.entity.player.WrappedPlayerInfo;
import me.googas.chat.packet.entity.player.WrappedPlayerInfoAction;
import me.googas.commands.util.Pair;
import me.googas.reflect.modifiers.CollectionModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class PlayerTabView implements TabView {

  @NonNull private final UUID viewer;
  @NonNull private final List<TabSlot> slots;
  @NonNull private final TabSize size;
  private boolean destroyed;

  public PlayerTabView(@NonNull UUID viewer, @NonNull TabSize size) {
    this.viewer = viewer;
    this.slots = new ArrayList<>();
    this.size = size;
    this.destroyed = false;
  }

  private static List<WrappedPlayerInfo> collectSlotsPlayerInfo(
      @NonNull Collection<TabSlot> slots, @NonNull Player player, @NonNull Packet packet) {
    return slots.stream()
        .map(
            slot -> {
              try {
                return slot.playerInfoData(player, packet);
              } catch (PacketHandlingException
                  | InvocationTargetException
                  | InstantiationException
                  | IllegalAccessException e) {
                ErrorHandler.getInstance()
                    .handle(Level.SEVERE, "Could not get PlayerInfoData for slot " + slot);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @NonNull
  public UUID getUniqueId() {
    return viewer;
  }

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
                Packet packet = PacketType.Play.ClientBound.PLAYER_INFO.create();
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

  @NonNull
  private List<WrappedPlayerInfo> collectSlotsPlayerInfo(Player player, Packet packet) {
    return collectSlotsPlayerInfo(slots, player, packet);
  }

  @Override
  public void initialize()
      throws PacketHandlingException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    if (destroyed || !slots.isEmpty()) return;
    Optional<Player> optional = this.getViewer();
    if (optional.isPresent()) {
      Player player = optional.get();
      Packet packet = PacketType.Play.ClientBound.PLAYER_INFO.create();
      List<WrappedPlayerInfo> info = this.populate(packet, player);
      packet.setField(0, WrappedPlayerInfoAction.ADD_PLAYER);
      packet.setField(1, CollectionModifier.addWrappers(info));
      packet.send(player);
    } else {
      destroyed = true;
    }
  }

  @NonNull
  private List<WrappedPlayerInfo> populate(@NonNull Packet packet, @NonNull Player player)
      throws PacketHandlingException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
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
   * Get the online players as {@link WrappedPlayerInfo}
   *
   * @deprecated This may no longer be needed as it is safer to push the players out of the tab list
   * @param packet the packet that will send the player info
   * @return the wrapped player info
   */
  private @NonNull List<WrappedPlayerInfo> getPlayers(@NonNull Packet packet) {
    return Bukkit.getOnlinePlayers().stream()
        .map(WrappedCraftPlayer::of)
        .map(
            craftPlayer -> {
              try {
                return craftPlayer.getHandle().playerInfo(packet);
              } catch (PacketHandlingException
                  | InvocationTargetException
                  | IllegalAccessException e) {
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
    this.set(slot, entry, updatesSkin(slot, entry));
  }

  public void set(@NonNull TabSlot slot, @NonNull TabEntry entry, boolean skin) {
    slot.setEntry(entry);
    this.update(Collections.singleton(slot), skin);
  }

  private void update(@NonNull Collection<TabSlot> slots, boolean skin) {
    this.getViewer()
        .ifPresent(
            player -> {
              try {
                Packet packet = PacketType.Play.ClientBound.PLAYER_INFO.create();
                packet.setField(
                    1,
                    CollectionModifier.addWrappers(collectSlotsPlayerInfo(slots, player, packet)));
                if (skin) {
                  packet.setField(0, WrappedPlayerInfoAction.REMOVE_PLAYER);
                  packet.send(player);
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
    return add(Collections.singleton(entry));
  }

  @Override
  public boolean add(@NonNull Collection<TabEntry> entries) {
    Map<TabSlot, TabEntry> toUpdate = new HashMap<>();
    Pair<List<TabSlot>, List<TabEntry>> pair = getReplacements(entries);
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
    slots.stream().filter(slot -> {
      for (TabEntry entry : entries) {
        if (entry.equals(slot.getEntry())) {
          return true;
        }
      }
      return false;
    }).forEach(slot -> toUpdate.put(slot, replacement.get()));
    this.set(toUpdate);
    return toUpdate.size() > 0;
  }

  @Override
  public void sort() {
    Map<TabSlot, TabEntry> toUpdate = new HashMap<>();
    List<TabEntry> sortedEntries = this.slots.stream().map(TabSlot::getEntry).sorted().collect(Collectors.toList());
    for (int i = 0; i < slots.size(); i++) {
      TabSlot tabSlot = slots.get(i);
      TabEntry tabEntry = sortedEntries.get(i);
      if (!tabSlot.getEntry().equals(tabEntry)) {
        toUpdate.put(tabSlot, tabEntry);
      }
    }
    set(toUpdate);
  }

  @NonNull
  private Pair<List<TabSlot>, List<TabEntry>> getReplacements(
      @NonNull Collection<TabEntry> entries) {
    return getReplacements(entries, (slot, entry) -> slot.getEntry().canBeReplaced(entry));
  }

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
          boolean skin = updatesSkin(slot, entry);
          slot.setEntry(entry);
          if (skin) {
            updateSkin.add(slot);
          } else {
            update.add(slot);
          }
        });
    update(update, false);
    update(updateSkin, true);
  }

  private boolean updatesSkin(@NonNull TabSlot slot, @NonNull TabEntry entry) {
    return slot.getEntry().getSkin() == null && entry.getSkin() != null
        || !slot.getEntry().getSkin().equals(entry.getSkin());
  }
}
