package me.googas.chat.api.lines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.util.Strings;

/** This is a {@link Line} which uses a message obtained from {@link ResourceManager}. */
public final class Localized implements Line {

  @NonNull @Getter private final Locale locale;
  @NonNull @Getter private final List<Line> extra;
  @NonNull private String text;

  Localized(@NonNull Locale locale, @NonNull String text) {
    this.locale = locale;
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Collection<Line> extra) {
    return (Localized) Line.super.appendMany(extra);
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Line... lines) {
    return (Localized) Line.super.appendMany(lines);
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
  public @NonNull Localized copy() {
    return new Localized(locale, text).appendMany(this.extra);
  }

  @Override
  public @NonNull Localized format(@NonNull Object... objects) {
    text = Strings.format(text, objects);
    this.extra.forEach(line -> line.format(objects));
    return this;
  }

  public @NonNull Localized format(@NonNull Map<String, String> map) {
    text = Strings.format(text, map);
    this.extra.forEach(line -> line.format(map));
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Formatter formatter) {
    return (Localized) formatter.format(this);
  }

  @Override
  public @NonNull Localized append(@NonNull Line line) {
    this.extra.add(line);
    return this;
  }

  @Override
  public @NonNull Line format(@NonNull Placeholder placeholder) {
    this.text = placeholder.format(this.text);
    this.extra.forEach(line -> line.format(placeholder));
    return this;
  }

  @Override
  public @NonNull Localized append(@NonNull String string) {
    return (Localized) Line.super.append(string);
  }

  /** Represents a formatter which can format {@link Line} using {@link Locale}. */
  public interface LocalizedFormatter {
    /**
     * Format the line.
     *
     * @param locale the locale to format the line with
     * @param line the line to format
     * @return the formatted line
     */
    @NonNull
    Line format(@NonNull Locale locale, @NonNull Line line);
  }
}
