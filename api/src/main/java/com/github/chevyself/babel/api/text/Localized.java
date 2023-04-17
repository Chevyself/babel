package com.github.chevyself.babel.api.text;

import chevyself.github.commands.util.Strings;
import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.text.format.Formatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

/** This is a {@link Text} which uses a message obtained from {@link ResourceManager}. */
public final class Localized implements Text {

  @NonNull @Getter private final Locale locale;
  @NonNull @Getter private final List<Text> extra;
  @NonNull private String text;
  @Getter private boolean sample = false;
  private boolean hasPlaceholders = false;

  Localized(@NonNull Locale locale, @NonNull String text) {
    this.locale = locale;
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Collection<Text> extra) {
    return (Localized) Text.super.appendMany(extra);
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Text... texts) {
    return (Localized) Text.super.appendMany(texts);
  }

  @Override
  public @NonNull String getRaw() {
    return text;
  }

  @NonNull
  public Localized setRaw(@NonNull String json) {
    this.text = json;
    return this;
  }

  @Override
  public boolean hasPlaceholders() {
    return this.hasPlaceholders;
  }

  @Override
  public @NonNull Localized setSample(boolean sample) {
    this.sample = sample;
    return this;
  }

  @Override
  public @NonNull Localized setHasPlaceholders(boolean hasPlaceholders) {
    this.hasPlaceholders = hasPlaceholders;
    return this;
  }

  @Override
  public @NonNull Localized copy() {
    return new Localized(locale, text).appendMany(this.extra);
  }

  @Override
  public @NonNull Localized format(@NonNull Object... objects) {
    text = Strings.format(text, objects);
    this.extra.forEach(text -> text.format(objects));
    return this;
  }

  public @NonNull Localized format(@NonNull Map<String, String> map) {
    text = Strings.format(text, map);
    this.extra.forEach(text -> text.format(map));
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Formatter formatter) {
    return (Localized) formatter.format(this);
  }

  @Override
  public @NonNull Localized append(@NonNull Text text) {
    this.extra.add(text);
    return this;
  }

  @Override
  public @NonNull Text format(@NonNull Placeholder placeholder) {
    this.text = placeholder.format(this.text);
    this.extra.forEach(text -> text.format(placeholder));
    return this;
  }

  @Override
  public @NonNull Localized append(@NonNull String string) {
    return (Localized) Text.super.append(string);
  }

  /** Represents a formatter which can format {@link Text} using {@link Locale}. */
  public interface LocalizedFormatter {
    /**
     * Format text.
     *
     * @param locale the locale to format the text with
     * @param text the text to format
     * @return the formatted text
     */
    @NonNull
    Text format(@NonNull Locale locale, @NonNull Text text);
  }
}
