package com.github.chevyself.babel.api.tab;

import com.github.chevyself.babel.adapters.PlayerTabViewAdapter;
import com.github.chevyself.babel.adapters.tab.PlayerInfoAdapter;
import com.github.chevyself.babel.api.tab.entries.EmptyTabEntry;
import com.github.chevyself.babel.api.tab.entries.TabEntry;
import com.github.chevyself.babel.api.util.Players;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.starbox.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** Implementation of {@link TabView} for a player. */
public class PlayerTabView implements TabView {
  @NonNull private static final PlayerTabViewAdapter adapter = Players.getTabViewAdapter();

  @NonNull @Getter private final UUID uniqueId;
  @NonNull @Getter private final TabSize size;
  @NonNull private final List<TabSlot> slots;
  @Getter private boolean destroyed;
  private boolean initialized;

  /**
   * Create a new tab view for a player.
   *
   * @param uniqueId the unique id of the player
   * @param size the size of the tab view
   */
  public PlayerTabView(@NonNull UUID uniqueId, @NonNull TabSize size) {
    this.uniqueId = uniqueId;
    this.size = size;
    this.slots = new ArrayList<>();
  }

  /**
   * Converts the slots into a list of {@link PlayerInfoAdapter} to send to the client.
   *
   * @param slots the slots to convert
   * @param player the player that will view the entries
   * @return the list of player info
   * @throws NullPointerException if any argument is null
   */
  private static List<PlayerInfoAdapter> collectSlotsAdapters(
      @NonNull Collection<TabSlot> slots, @NonNull Player player) {
    return slots.stream()
        .map(
            slot -> {
              try {
                return slot.toAdapter(player);
              } catch (PacketHandlingException e) {
                Debugger.getInstance()
                    .getLogger()
                    .log(Level.SEVERE, "Failed to create adapter for slot", e);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Get the viewer of this tab view.
   *
   * @return the viewer
   */
  @NonNull
  public Optional<Player> getViewer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.uniqueId));
  }

  @Override
  public void clear() {
    Map<TabSlot, TabEntry> map = new HashMap<>();
    for (TabCoordinate coordinate : this.size) {
      map.put(this.getSlot(coordinate), new EmptyTabEntry());
    }
    this.set(map);
  }

  @Override
  public void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry) {
    TabSlot slot = this.getSlot(coordinate);
    this.set(slot, entry, this.updatesSkin(slot, entry));
  }

  @Override
  public void initialize() {
    if (!this.initialized) {
      this.initialized = true;
      this.checkForViewer(
          viewer -> {
            try {
              PlayerTabView.adapter.initialize(viewer, this.populate(viewer));
            } catch (PacketHandlingException e) {
              Debugger.getInstance()
                  .getLogger()
                  .log(Level.SEVERE, "Could not initialize tab view", e);
            }
          });
    }
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
  public boolean add(@NonNull TabEntry entry) {
    return this.add(Collections.singletonList(entry));
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

  /**
   * Update the slots view in the client.
   *
   * @param slots the slots to update
   * @param skin whether the skin of the slots should be updated
   */
  private void update(@NonNull Collection<TabSlot> slots, boolean skin) {
    this.checkForViewer(
        player -> {
          PlayerTabView.adapter.update(
              player, skin, PlayerTabView.collectSlotsAdapters(slots, player));
        });
  }

  @NonNull
  private TabSlot getSlot(TabCoordinate coordinate) {
    return this.slots.stream()
        .filter(slot -> slot.getCoordinate().equals(coordinate))
        .findFirst()
        .orElseThrow(
            () ->
                new IndexOutOfBoundsException("Could not find slot with coordinate " + coordinate));
  }

  @NonNull
  private List<PlayerInfoAdapter> populate(@NonNull Player viewer) throws PacketHandlingException {
    List<PlayerInfoAdapter> adapters = new ArrayList<>();
    for (TabCoordinate coordinate : this.size) {
      TabSlot slot = new TabSlot(coordinate, new EmptyTabEntry());
      this.slots.add(slot);
      adapters.add(slot.toAdapter(viewer));
    }
    return adapters;
  }

  private List<PlayerInfoAdapter> collectSlotsAdapters(@NonNull Player player) {
    return PlayerTabView.collectSlotsAdapters(this.slots, player);
  }

  /**
   * Checks that the viewer is still in the server, else it will destroy the tab view.
   *
   * @param consumer the consumer to accept the viewer in case it is present
   */
  protected void checkForViewer(@NonNull Consumer<@NonNull Player> consumer) {
    Optional<Player> optional = this.getViewer();
    if (optional.isPresent()) {
      consumer.accept(optional.get());
    } else {
      this.destroy();
    }
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

  private void destroy() {
    this.destroyed = true;
  }
}
