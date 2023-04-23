package com.github.chevyself.babel.adapters.tab;

import com.github.chevyself.babel.adapters.tab.entries.EmptyTabEntry;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import lombok.NonNull;

/**
 * Represents the view of a tab list. This is the list that is shown to the player when they press
 * tab
 */
public interface TabView {

  /** Remove all the entries from the tab list. */
  void clear();

  /**
   * Initialize the tab list. This will send the packets to the player to initialize the tab list.
   *
   * @throws PacketHandlingException if there was an error sending the packets
   * @throws InvocationTargetException if there was an error invoking the constructor of the entry
   * @throws InstantiationException if there was an error instantiating the entry
   * @throws IllegalAccessException if there was an error accessing the constructor of the entry
   */
  void initialize()
      throws PacketHandlingException, InvocationTargetException, InstantiationException,
          IllegalAccessException;

  /**
   * Set the entry at the given coordinate.
   *
   * @param coordinate the coordinate to set the entry
   * @param entry the entry to set
   * @throws NullPointerException if the coordinate or entry is null
   */
  void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry);

  /**
   * Set the entry at the given coordinate.
   *
   * @param x the x coordinate to set the entry
   * @param y the y coordinate to set the entry
   * @param entry the entry to set
   * @throws NullPointerException if the entry is null
   */
  default void set(int x, int y, @NonNull TabEntry entry) {
    this.set(new TabCoordinate(x, y), entry);
  }

  /**
   * Get the size of the tab view.
   *
   * @return the size of the tab
   */
  @NonNull
  TabSize getSize();

  /**
   * Adds an entry to the tab list. This will add the entry to the first empty slot.
   *
   * @param entry the entry to add
   * @return true if the entry was added
   * @throws NullPointerException if the entry is null
   */
  boolean add(@NonNull TabEntry entry);

  /**
   * Adds many entries to the tab list. It will iterate through the entries and add them to the
   * first empty slot.
   *
   * @see #add(TabEntry)
   * @param entries the entries to add
   * @return true if at least one entry was added
   * @throws NullPointerException if the entries is null
   */
  boolean add(@NonNull Collection<TabEntry> entries);

  /**
   * Removes entries from the tab list. If the entry of a slot matches the entry to remove it will
   * be replaced with {@link EmptyTabEntry}
   *
   * @param entries the entries to remove
   * @return true if at least one entry was removed
   * @throws NullPointerException if the entries is null
   */
  default boolean remove(@NonNull TabEntry... entries) {
    return this.remove(EmptyTabEntry::new, entries);
  }

  /**
   * Removes entries from the tab list. If the entry of a slot matches the entry to remove it will
   * be replaced with {@link EmptyTabEntry}
   *
   * @param entries the entries to remove
   * @return true if at least one entry was removed
   * @throws NullPointerException if the entries is null
   */
  default boolean remove(@NonNull Collection<? extends TabEntry> entries) {
    return this.remove(EmptyTabEntry::new, entries);
  }

  /**
   * Removes the entries from the tab list and replaces it with the entry given by the supplier.
   *
   * @param replacement the supplier of the entry to replace the removed entries
   * @param entries the entries to remove
   * @return true if at least one entry was removed
   */
  default boolean remove(@NonNull Supplier<TabEntry> replacement, TabEntry... entries) {
    return this.remove(replacement, Arrays.asList(entries));
  }

  /**
   * Removes the entries from the tab list and replaces it with the entry given by the supplier.
   *
   * @param replacement the supplier of the entry to replace the removed entries
   * @param entries the entries to remove
   * @return true if at least one entry was removed
   */
  boolean remove(
      @NonNull Supplier<TabEntry> replacement, @NonNull Collection<? extends TabEntry> entries);

  /** Sorts the entries inside the slots of the tab view. */
  void sort();
}
