package me.googas.chat.api.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.text.format.Formatter;
import me.googas.commands.util.Strings;

/** Represents plain text. */
public final class Plain implements Text {

  @NonNull @Getter private final List<Text> extra;
  @NonNull private String text;

  Plain(@NonNull String text) {
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull Plain copy() {
    return new Plain(text).appendMany(this.extra);
  }

  @Override
  public @NonNull Plain appendMany(@NonNull Collection<Text> extra) {
    return (Plain) Text.super.appendMany(extra);
  }

  @Override
  public @NonNull Plain append(@NonNull String string) {
    return (Plain) Text.super.append(string);
  }

  @Override
  public @NonNull Plain appendMany(@NonNull Text... texts) {
    return (Plain) Text.super.appendMany(texts);
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
    this.extra.forEach(text -> text.format(objects));
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Formatter formatter) {
    return (Plain) formatter.format(this);
  }

  @Override
  public @NonNull Plain append(@NonNull Text text) {
    this.extra.add(text);
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Placeholder placeholder) {
    this.text = placeholder.format(this.text);
    this.extra.forEach(text -> text.format(placeholder));
    return this;
  }
}
