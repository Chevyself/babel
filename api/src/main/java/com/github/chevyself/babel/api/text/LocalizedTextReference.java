package com.github.chevyself.babel.api.text;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.lang.Language;
import com.github.chevyself.babel.api.text.format.Formatter;
import com.github.chevyself.reflect.debug.Debugger;
import java.util.*;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

/** A {@link Text} that references a language key to be built into {@link LocalizedText}. */
public final class LocalizedTextReference implements Text {

  /** Objects formatters. */
  @NonNull private final List<Object> objects;
  /** Placeholders formatters. */
  @NonNull private final Map<String, String> placeholders;
  /** Formatters. */
  @NonNull private final List<Formatter> formatters;

  @NonNull private final List<Placeholder> linePlaceholders;

  @NonNull @Getter private final List<Text> extra;

  @NonNull private final String key;
  @Getter private final boolean sample;
  private final boolean hasPlaceholders;

  LocalizedTextReference(@NonNull String key) {
    this(
        new ArrayList<>(),
        new HashMap<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        key,
        false,
        false);
  }

  private LocalizedTextReference(
      @NonNull List<Object> objects,
      @NonNull Map<String, String> placeholders,
      @NonNull List<Formatter> formatters,
      @NonNull List<Placeholder> linePlaceholders,
      @NonNull List<Text> extra,
      @NonNull String key,
      boolean sample,
      boolean hasPlaceholders) {
    this.objects = objects;
    this.placeholders = placeholders;
    this.formatters = formatters;
    this.linePlaceholders = linePlaceholders;
    this.extra = extra;
    this.key = key;
    this.sample = sample;
    this.hasPlaceholders = hasPlaceholders;
  }

  /**
   * Get the {@link LocalizedText} that this references to.
   *
   * @param locale the locale to get the raw message
   * @return the {@link LocalizedText}
   */
  public @NonNull LocalizedText asLocalized(@NonNull Locale locale) {
    LocalizedText localized = Text.localized(locale, this.key);
    if (!extra.isEmpty()) localized.appendMany(this.extra);
    if (!objects.isEmpty()) localized.format(objects.toArray());
    if (!placeholders.isEmpty()) localized.format(placeholders);
    if (!formatters.isEmpty()) localized.format(formatters);
    if (!linePlaceholders.isEmpty())
      localized.placeholders(linePlaceholders.toArray(new Placeholder[0]));
    return localized.setSample(this.sample).setHasPlaceholders(this.hasPlaceholders);
  }

  /**
   * Get the {@link LocalizedText} that this references to.
   *
   * @param sender the sender to get the locale from
   * @return the {@link LocalizedText}
   */
  public @NonNull LocalizedText asLocalized(@NonNull CommandSender sender) {
    return this.asLocalized(Language.getLocale(sender));
  }

  /**
   * Get the {@link LocalizedText} that this references to.
   *
   * @param channel the channel to get the locale from
   * @return the {@link LocalizedText}
   */
  public @NonNull LocalizedText asLocalized(@NonNull Channel channel) {
    return this.asLocalized(channel.getLocale().orElse(ResourceManager.getBase()));
  }

  /**
   * Raw use of {@link LocalizedText}. This will warn the {@link java.util.logging.Logger} when used
   *
   * @return the {@link LocalizedText}
   */
  public @NonNull LocalizedText asLocalized() {
    return this.asLocalized(ResourceManager.getBase());
  }

  @Override
  public boolean hasPlaceholders() {
    return this.hasPlaceholders;
  }

  @Override
  public @NonNull Text setSample(boolean sample) {
    return this.copy(sample, this.hasPlaceholders);
  }

  @Override
  public @NonNull Text setHasPlaceholders(boolean placeholders) {
    return this.copy(this.sample, placeholders);
  }

  @Override
  public @NonNull LocalizedTextReference copy() {
    return this.copy(this.sample, this.hasPlaceholders);
  }

  public @NonNull LocalizedTextReference copy(boolean sample, boolean hasPlaceholders) {
    return new LocalizedTextReference(
        new ArrayList<>(this.objects),
        new HashMap<>(this.placeholders),
        new ArrayList<>(this.formatters),
        new ArrayList<>(linePlaceholders),
        new ArrayList<>(this.extra),
        this.key,
        sample,
        hasPlaceholders);
  }

  @Override
  public @NonNull LocalizedTextReference appendMany(@NonNull Collection<Text> extra) {
    return (LocalizedTextReference) Text.super.appendMany(extra);
  }

  @Override
  public @NonNull LocalizedTextReference appendMany(@NonNull Text... texts) {
    return (LocalizedTextReference) Text.super.appendMany(texts);
  }

  @Override
  public @NonNull BaseComponent[] build() {
    Debugger.getInstance().getLogger().finest("Raw use of LocalizedTextReference#build");
    return this.asLocalized().build();
  }

  @Override
  public BaseComponent[] build(@NonNull Channel channel) {
    return this.asLocalized(channel).build();
  }

  @Override
  public @NonNull String asString(@NonNull Channel channel) {
    return this.asLocalized(channel).asString();
  }

  @Override
  public @NonNull LocalizedTextReference format(@NonNull Object... objects) {
    this.objects.addAll(Arrays.asList(objects));
    this.extra.forEach(text -> text.format(objects));
    return this;
  }

  @Override
  public @NonNull LocalizedTextReference format(@NonNull Formatter formatter) {
    this.formatters.add(formatter);
    return this;
  }

  @Override
  public @NonNull LocalizedTextReference append(@NonNull Text text) {
    this.extra.add(text);
    return this;
  }

  @Override
  public @NonNull LocalizedTextReference format(@NonNull Placeholder placeholder) {
    this.linePlaceholders.add(placeholder);
    return this;
  }

  @Override
  public @NonNull String getRaw() {
    Debugger.getInstance().getLogger().finest("Raw use of LocalizedTextReference#getRaw");
    return this.asLocalized().getRaw();
  }

  @Override
  public @NonNull LocalizedTextReference setRaw(@NonNull String raw) {
    throw new UnsupportedOperationException("Cannot change the key of a LocalizedTextReference");
  }

  @Override
  public @NonNull LocalizedTextReference append(@NonNull String string) {
    return (LocalizedTextReference) Text.super.append(string);
  }
}
