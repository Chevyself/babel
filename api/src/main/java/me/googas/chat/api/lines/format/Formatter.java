package me.googas.chat.api.lines.format;

import lombok.NonNull;
import me.googas.chat.api.lines.Line;

/** This represents a formatter for lines. */
public interface Formatter {

  /**
   * Format a line.
   *
   * @param line the line to be formatted
   * @return the formatted line
   */
  @NonNull
  Line format(@NonNull Line line);
}
