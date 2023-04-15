package me.googas.chat.api.tab.entries;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.lines.Line;

/** A tab entry that represents a text. */
public class TextTabEntry extends EmptyTabEntry {

  @Getter @NonNull private final Line display;

  /**
   * Create the entry
   *
   * @param display the line to display
   */
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
