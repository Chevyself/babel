package com.github.chevyself.babel.api.exceptions;

import lombok.NonNull;

public class LanguageParsingException extends Exception {
  public LanguageParsingException(@NonNull String message) {
    super(message);
  }
}
