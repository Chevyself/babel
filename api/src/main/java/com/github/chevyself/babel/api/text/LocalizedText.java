package com.github.chevyself.babel.api.text;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.text.format.Formatter;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

/** This is a {@link Text} which uses a message obtained from {@link ResourceManager}. */
public final class LocalizedText implements Text {

  @NonNull @Getter private final Locale locale;
  @NonNull @Getter private final List<Text> extra;
  @NonNull private String text;
  @Getter private boolean sample = false;
  private boolean hasPlaceholders = false;

  LocalizedText(@NonNull Locale locale, @NonNull String text) {
    this.locale = locale;
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull LocalizedText appendMany(@NonNull Collection<Text> extra) {
    return (LocalizedText) Text.super.appendMany(extra);
  }

  @Override
  public @NonNull LocalizedText appendMany(@NonNull Text... texts) {
    return (LocalizedText) Text.super.appendMany(texts);
  }

  @Override
  public @NonNull String getRaw() {
    return text;
  }

  @NonNull
  public LocalizedText setRaw(@NonNull String json) {
    this.text = json;
    return this;
  }

  @Override
  public boolean hasPlaceholders() {
    return this.hasPlaceholders;
  }

  @Override
  public @NonNull LocalizedText setSample(boolean sample) {
    this.sample = sample;
    return this;
  }

  @Override
  public @NonNull LocalizedText setHasPlaceholders(boolean hasPlaceholders) {
    this.hasPlaceholders = hasPlaceholders;
    return this;
  }

  @Override
  public @NonNull LocalizedText copy() {
    return new LocalizedText(locale, text).appendMany(this.extra);
  }

  @Override
  public @NonNull LocalizedText format(@NonNull Object... objects) {
    text = Strings.format(text, objects);
    this.extra.forEach(text -> text.format(objects));
    return this;
  }

  public @NonNull LocalizedText format(@NonNull Map<String, String> map) {
    // TODO this must be moved to Text#format(Map<String, String>)
    text = Strings.format(text, map);
    this.extra.forEach(text -> text.format(map));
    return this;
  }

  @Override
  public @NonNull LocalizedText format(@NonNull Formatter formatter) {
    return (LocalizedText) formatter.format(this);
  }

  @Override
  public @NonNull LocalizedText append(@NonNull Text text) {
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
  public @NonNull LocalizedText append(@NonNull String string) {
    return (LocalizedText) Text.super.append(string);
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
