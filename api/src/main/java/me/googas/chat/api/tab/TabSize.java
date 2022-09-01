package me.googas.chat.api.tab;

import lombok.Getter;
import lombok.NonNull;

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
    return new TabSize.Iterator(this);
  }

  public class Iterator implements java.util.Iterator<TabCoordinate> {

    @NonNull private final TabSize size;
    int index = 0;

    public Iterator(@NonNull TabSize size) {
      this.size = size;
    }

    @Override
    public boolean hasNext() {
      return index < size.getTotal();
    }

    @Override
    public TabCoordinate next() {
      TabCoordinate coordinate = new TabCoordinate(index % size.getRows(), index / size.getRows());
      index++;
      return coordinate;
    }
  }
}
