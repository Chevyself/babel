package me.googas.chat.api.text.format;

import lombok.NonNull;
import me.googas.chat.api.text.Text;

/** This represents a formatter for lines. */
public interface Formatter {

  /**
   * Format text.
   *
   * @param text the text to be formatted
   * @return the formatted text
   */
  @NonNull
  Text format(@NonNull Text text);
}
