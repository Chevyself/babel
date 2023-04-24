package com.github.chevyself.babel.api.text;

import com.github.chevyself.babel.api.text.format.Formatter;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/** Represents plain text. */
public final class PlainText implements Text {

  @NonNull @Getter private final List<Text> extra;
  @NonNull private String text;
  @Getter private boolean sample = false;
  private boolean hasPlaceholders = false;

  PlainText(@NonNull String text) {
    this.text = text;
    this.extra = new ArrayList<>();
  }

  @Override
  public boolean hasPlaceholders() {
    return this.hasPlaceholders;
  }

  @Override
  public @NonNull Text setSample(boolean sample) {
    this.sample = sample;
    return this;
  }

  @Override
  public @NonNull Text setHasPlaceholders(boolean hasPlaceholders) {
    this.hasPlaceholders = hasPlaceholders;
    return this;
  }

  @Override
  public @NonNull PlainText copy() {
    return new PlainText(text).appendMany(this.extra);
  }

  @Override
  public @NonNull PlainText appendMany(@NonNull Collection<Text> extra) {
    return (PlainText) Text.super.appendMany(extra);
  }

  @Override
  public @NonNull PlainText append(@NonNull String string) {
    return (PlainText) Text.super.append(string);
  }

  @Override
  public @NonNull PlainText appendMany(@NonNull Text... texts) {
    return (PlainText) Text.super.appendMany(texts);
  }

  @Override
  public @NonNull String getRaw() {
    return this.text;
  }

  @Override
  public @NonNull PlainText setRaw(@NonNull String raw) {
    this.text = raw;
    return this;
  }

  @Override
  public @NonNull PlainText format(@NonNull Object... objects) {
    this.text = Strings.format(text, objects);
    this.extra.forEach(text -> text.format(objects));
    return this;
  }

  @Override
  public @NonNull PlainText format(@NonNull Formatter formatter) {
    return (PlainText) formatter.format(this);
  }

  @Override
  public @NonNull PlainText append(@NonNull Text text) {
    this.extra.add(text);
    return this;
  }

  @Override
  public @NonNull PlainText format(@NonNull Placeholder placeholder) {
    this.text = placeholder.format(this.text);
    this.extra.forEach(text -> text.format(placeholder));
    return this;
  }
}
