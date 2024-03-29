package com.github.chevyself.babel.api.tab;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the size of the tab list. This is the number of rows and slots that the tab list has.
 */
@Getter
public enum TabSize implements Iterable<TabCoordinate> {
  ONE(20, 1),
  TWO(40, 2),
  THREE(60, 3),
  FOUR(80, 4);

  private final int total;
  private final int rows;

  TabSize(int total, int rows) {
    this.total = total;
    this.rows = rows;
  }

  @NonNull
  @Override
  public TabSize.Iterator iterator() {
    return new Iterator(this);
  }

  /** Iterator for the slots of the size of the tab list. */
  public static class Iterator implements java.util.Iterator<TabCoordinate> {

    @NonNull private final TabSize size;
    int x = 0;
    int y = 0;

    /**
     * Create a new iterator for the slots of the tab list.
     *
     * @param size the size of the tab list
     */
    public Iterator(@NonNull TabSize size) {
      this.size = size;
    }

    @Override
    public boolean hasNext() {
      return x < size.getRows();
    }

    @Override
    public TabCoordinate next() {
      TabCoordinate coordinate = new TabCoordinate(x, y);
      y++;
      if (y >= size.getTotal() / size.getRows()) {
        y = 0;
        x++;
      }
      return coordinate;
    }
  }
}
