package me.googas.chat.api.tab;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import lombok.NonNull;
import me.googas.chat.api.tab.entries.EmptyTabEntry;
import me.googas.chat.api.tab.entries.TabEntry;
import me.googas.chat.exceptions.PacketHandlingException;

public interface TabView {

  void clear();

  void initialize()
      throws PacketHandlingException, InvocationTargetException, InstantiationException,
          IllegalAccessException;

  void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry);

  default void set(int x, int y, @NonNull TabEntry entry) {
    this.set(new TabCoordinate(x, y), entry);
  }

  @NonNull
  TabSize getSize();

  boolean add(@NonNull TabEntry entry);

  boolean add(@NonNull Collection<TabEntry> entries);

  default boolean remove(@NonNull TabEntry... entries) {
    return this.remove(EmptyTabEntry::new, entries);
  }

  default boolean remove(@NonNull Collection<? extends TabEntry> entries) {
    return this.remove(EmptyTabEntry::new, entries);
  }

  default boolean remove(@NonNull Supplier<TabEntry> replacement, TabEntry... entries) {
    return this.remove(replacement, Arrays.asList(entries));
  }

  boolean remove(
      @NonNull Supplier<TabEntry> replacement, @NonNull Collection<? extends TabEntry> entries);

  void sort();
}
