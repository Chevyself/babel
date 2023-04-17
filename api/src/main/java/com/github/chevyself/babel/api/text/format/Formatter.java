package com.github.chevyself.babel.api.text.format;

import com.github.chevyself.babel.api.text.Text;
import lombok.NonNull;

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
