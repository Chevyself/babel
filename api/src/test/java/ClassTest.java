import java.util.*;
import lombok.NonNull;
import me.googas.chat.api.tab.TabCoordinate;
import me.googas.chat.api.tab.TabSize;
import me.googas.chat.api.tab.TabSlot;
import me.googas.chat.api.tab.entries.EmptyTabEntry;

public class ClassTest {

  @NonNull private static final List<TabSlot> slots = new ArrayList<>();

  static {
    for (TabCoordinate coordinate : TabSize.FOUR) {
      slots.add(new TabSlot(coordinate, new EmptyTabEntry()));
    }
  }

  public static void main(String[] args) throws NoSuchMethodException {
    Collections.sort(slots);
    for (TabSlot slot : slots) {
      System.out.println(slot);
    }
  }
}
