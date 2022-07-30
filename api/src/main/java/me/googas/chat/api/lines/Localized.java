package me.googas.chat.api.lines;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.bukkit.utils.Components;
import me.googas.commands.util.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/** This is a {@link Line} which uses a message obtained from {@link ResourceManager}. */
public final class Localized implements Line {

  @NonNull @Getter private final Locale locale;
  @NonNull private String json;

  Localized(@NonNull Locale locale, @NonNull String json) {
    this.locale = locale;
    this.json = json;
  }

  @Override
  public @NonNull String getRaw() {
    return json;
  }

  @NonNull
  public Localized setRaw(@NonNull String json) {
    this.json = json;
    return this;
  }

  @Override
  public @NonNull Localized copy() {
    return new Localized(locale, json);
  }

  @Override
  public @NonNull BaseComponent[] build() {
    return Components.getComponent(json);
  }

  @Override
  public @NonNull Optional<String> asText() {
    return Optional.of(new TextComponent(this.build()).toLegacyText());
  }

  @Override
  public @NonNull Localized format(@NonNull Object... objects) {
    json = Strings.format(json, objects);
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Map<String, String> map) {
    json = Strings.format(json, map);
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Formatter formatter) {
    return (Localized) formatter.format(this);
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
