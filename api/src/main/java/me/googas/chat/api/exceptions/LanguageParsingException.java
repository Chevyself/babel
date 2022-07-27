package me.googas.chat.api.exceptions;

import lombok.NonNull;

public class LanguageParsingException extends Exception {
  public LanguageParsingException(@NonNull String message) {
    super(message);
  }
}
