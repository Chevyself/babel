package com.github.chevyself.babel.api.tab.entries;

import com.github.chevyself.babel.api.text.Text;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

/** A tab entry that represents a text. */
public class TextTabEntry extends EmptyTabEntry {

  @Getter @NonNull private final Text display;

  /**
   * Create the entry
   *
   * @param display the line to display
   */
  public TextTabEntry(@NonNull Text display) {
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
