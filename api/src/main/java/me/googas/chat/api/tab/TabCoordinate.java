package me.googas.chat.api.tab;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the coordinate of a tab entry. This is the position of the entry in the tab list.
 *
 * <p>This class is immutable and thread-safe
 */
public final class TabCoordinate implements Comparable<TabCoordinate> {

  @Getter private final int x;
  @Getter private final int y;

  /**
   * Create the coordinate.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public TabCoordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TabCoordinate that = (TabCoordinate) o;
    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "TabCoordinate{" + "x=" + x + ", y=" + y + '}';
  }

  @Override
  public int compareTo(@NonNull TabCoordinate o) {
    return this.getY() == o.getY() ? 0 : Integer.compare(this.getX(), o.getX());
  }
}
