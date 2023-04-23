import com.github.chevyself.babel.api.tab.TabCoordinate;
import com.github.chevyself.babel.api.tab.TabSize;
import com.github.chevyself.babel.api.tab.TabSlot;
import com.github.chevyself.babel.api.tab.entries.EmptyTabEntry;
import java.util.*;
import lombok.NonNull;

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
