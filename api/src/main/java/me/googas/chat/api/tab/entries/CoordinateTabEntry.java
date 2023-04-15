package me.googas.chat.api.tab.entries;

import lombok.NonNull;
import me.googas.chat.api.text.Text;
import me.googas.chat.api.tab.TabCoordinate;
import me.googas.chat.api.tab.TabSlot;

/**
 * Displays the coordinate of the slot that contains this entry.
 *
 * @deprecated this class is only for testing purposes, to check that the tab sorting is working.
 */
public class CoordinateTabEntry extends EmptyTabEntry {
  @Override
  public @NonNull Text getDisplay(@NonNull TabSlot slot) {
    TabCoordinate coordinate = slot.getCoordinate();
    return Text.of(coordinate.getX() + ", " + coordinate.getY());
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof CoordinateTabEntry;
  }
}
