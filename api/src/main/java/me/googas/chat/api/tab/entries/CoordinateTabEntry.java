package me.googas.chat.api.tab.entries;

import lombok.NonNull;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.tab.TabCoordinate;
import me.googas.chat.api.tab.TabSlot;

public class CoordinateTabEntry extends EmptyTabEntry {
  @Override
  public @NonNull Line getDisplay(@NonNull TabSlot slot) {
    TabCoordinate coordinate = slot.getCoordinate();
    return Line.of(coordinate.getX() + ", " + coordinate.getY());
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof CoordinateTabEntry;
  }
}
