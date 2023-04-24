package com.github.chevyself.babel.api.exceptions;

import lombok.NonNull;

/** Thrown when a language file cannot be parsed. */
public class LanguageParsingException extends Exception {

  /**
   * Constructs a new {@code LanguageParsingException} with the specified detail message.
   *
   * @param message the detail message
   */
  public LanguageParsingException(@NonNull String message) {
    super(message);
  }
}
