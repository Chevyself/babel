package com.github.chevyself.babel.api.tab.entries;

import com.github.chevyself.babel.api.tab.TabCoordinate;
import com.github.chevyself.babel.api.tab.TabSlot;
import com.github.chevyself.babel.api.text.Text;
import lombok.NonNull;

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
