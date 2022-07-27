package me.googas.chat.api.lines;

import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bukkit.utils.Components;
import net.md_5.bungee.api.chat.BaseComponent;

/** Represents a plain text line. */
public final class Plain implements Line {

  @NonNull private String text;

  Plain(@NonNull String text) {
    this.text = text;
  }

  @Override
  public @NonNull Plain copy() {
    return new Plain(text);
  }

  @Override
  public @NonNull Result asResult() {
    return new Result(this.build());
  }

  @Override
  public @NonNull BaseComponent[] build() {
    return Components.deserializePlain('&', text);
  }

  @Override
  public @NonNull Optional<String> asText() {
    return Optional.of(BukkitUtils.format(text));
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
    this.text = String.format(text, objects);
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Map<String, String> map) {
    this.text = String.format(text, map);
    return this;
  }

  @Override
  public @NonNull Plain format(@NonNull Formatter formatter) {
    formatter.format(this);
    return this;
  }
}
