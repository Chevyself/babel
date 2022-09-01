package me.googas.chat.api.tab.entries;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.lines.Line;

public class TextTabEntry extends EmptyTabEntry {

  @Getter @NonNull private final Line display;

  public TextTabEntry(@NonNull Line display) {
    this.display = display;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    TextTabEntry that = (TextTabEntry) o;
    return display.equals(that.display);
  }

  @Override
  public int hashCode() {
    return Objects.hash(display);
  }
}
