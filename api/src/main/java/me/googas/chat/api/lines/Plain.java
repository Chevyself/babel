package me.googas.chat.api.lines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.util.Strings;

/** Represents a plain text line. */
public final class Plain implements Line {

  @NonNull @Getter private final List<Line> extra;
  @NonNull private String text;

  Plain(@NonNull String text) {
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull Plain formatSample(@NonNull Channel channel) {
    return (Plain) Line.super.formatSample(channel);
  }

  @Override
  public @NonNull Plain copy() {
    return new Plain(text).appendMany(this.extra);
  }

  @Override
  public @NonNull Plain formatSample() {
    return (Plain) Line.super.formatSample();
  }

  @Override
  public @NonNull Plain formatSample(@NonNull Locale locale) {
    return (Plain) Line.super.formatSample(locale);
  }

  @Override
  public @NonNull Plain appendMany(@NonNull Collection<Line> extra) {
    return (Plain) Line.super.appendMany(extra);
  }

  @Override
  public @NonNull Plain append(@NonNull String string) {
    return (Plain) Line.super.append(string);
  }

  @Override
  public @NonNull Plain appendMany(@NonNull Line... lines) {
    return (Plain) Line.super.appendMany(lines);
  }

  @Override
  public @NonNull String getRaw() {
    return this.text;
  }

  @Override
  public @NonNull Plain setRaw(@NonNull String raw) {
    this.text = raw;
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Object... objects) {
    this.text = Strings.format(text, objects);
    this.extra.forEach(line -> line.format(objects));
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Map<String, String> map) {
    this.text = Strings.format(text, map);
    this.extra.forEach(line -> line.format(map));
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Formatter formatter) {
    return (Plain) formatter.format(this);
  }

  @Override
  public @NonNull Plain append(@NonNull Line line) {
    this.extra.add(line);
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Placeholder placeholder) {
    this.text = placeholder.format(this.text);
    this.extra.forEach(line -> line.format(placeholder));
    return this;
  }
}
