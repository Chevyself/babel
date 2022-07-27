package me.googas.chat.api;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

public class LanguagesPack {

  @NonNull @Getter @Delegate private final List<Language> languages;

  public LanguagesPack(@NonNull List<Language> languages) {
    this.languages = languages;
  }
}
