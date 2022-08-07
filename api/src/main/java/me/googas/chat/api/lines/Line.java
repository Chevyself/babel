package me.googas.chat.api.lines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.ForwardingChannel;
import me.googas.chat.api.Language;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.chat.api.placeholders.PlaceholderManager;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bukkit.utils.Components;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public interface Line extends BukkitResult {

  /**
   * Start a localized line.
   *
   * @param locale the locale to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  @NonNull
  static Localized localized(@NonNull Locale locale, @NonNull String key) {
    return new Localized(locale, ResourceManager.getInstance().getRaw(locale, key).trim());
  }

  /**
   * Start a localized line.
   *
   * @param sender the sender to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  @NonNull
  static Localized localized(@NonNull CommandSender sender, @NonNull String key) {
    return Line.localized(Language.getLocale(sender), key);
  }

  /**
   * Start a localized line.
   *
   * @param channel the channel to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  @NonNull
  static Localized localized(@NonNull Channel channel, String key) {
    return Line.localized(channel.getLocale().orElse(ResourceManager.getBase()), key);
  }

  /**
   * Get the localized lines for a forwarding channel.
   *
   * @param forwardingChannel the forwarding channel to get the lines
   * @param key the key to get the json/text message
   * @return a {@link List} containing the lines
   */
  @NonNull
  static List<Localized> localized(
      @NonNull ForwardingChannel.Multiple forwardingChannel, @NonNull String key) {
    return forwardingChannel.getChannels().stream()
        .map(channel -> Line.localized(channel, key))
        .collect(Collectors.toList());
  }

  /**
   * Get a localized reference from a key.
   *
   * @param key the key of the localized message
   * @return ta new {@link LocalizedReference} instance
   */
  @NonNull
  static LocalizedReference localized(@NonNull String key) {
    return new LocalizedReference(key);
  }

  /**
   * Start a plain line.
   *
   * @param text the text of the line
   * @return a plain line
   */
  @NonNull
  static Plain of(@NonNull String text) {
    return new Plain(text);
  }

  /**
   * Parse a line from a string. If the string starts with 'localized:' a {@link LocalizedReference}
   * will be returned else a {@link Plain} will be provided
   *
   * @param string the string to parse
   * @return the parsed line
   */
  @NonNull
  static Line parse(@NonNull String string) {
    if (!string.contains(" ") && (string.startsWith("localized:") || string.startsWith("$"))) {
      if (string.startsWith("localized:")) {
        string = string.substring(10);
      } else if (string.startsWith("$")) {
        string = string.substring(1);
      }
      return Line.localized(string);
    }
    return Line.of(string);
  }

  /**
   * Parse a line from a string. If the string starts with 'localized:' a {@link Localized} will be
   * returned else a {@link Plain} will be provided
   *
   * @param locale the locale parsing the line
   * @param string the string to parse
   * @return the parsed line
   */
  @NonNull
  static Line parse(Locale locale, @NonNull String string) {
    if (!string.contains(" ")
        && (string.startsWith("localized:") || string.startsWith("$") && locale != null)) {
      if (string.startsWith("localized:")) {
        string = string.substring(10);
      } else if (string.startsWith("$")) {
        if (string.startsWith("${") && string.endsWith("}")) {
          string = string.substring(2, string.length() - 1);
        } else {
          string = string.substring(1);
        }
      }
      return Line.localized(locale, string);
    } else {
      return Line.of(string);
    }
  }

  /**
   * Parse a line from a string.
   *
   * @see #parse(Locale, String)
   * @param channel to get the locale from
   * @param string the string to parse
   * @return the parsed line
   */
  @NonNull
  static Line parse(Channel channel, @NonNull String string) {
    return parse(
        channel == null ? null : channel.getLocale().orElse(ResourceManager.getBase()), string);
  }

  /**
   * Copy this line.
   *
   * @return a new copied instance of this line
   */
  @NonNull
  Line copy();

  /**
   * Build the message.
   *
   * @return the built message
   */
  @NonNull
  default BaseComponent[] build() {
    return this.build(true);
  }

  /**
   * Build the message.
   *
   * @return the built message
   */
  @NonNull
  default BaseComponent[] build(boolean sample) {
    Line copy = this.copy();
    if (sample) {
      ResourceManager.getInstance().getSampleFormatter().format(ResourceManager.getBase(), copy);
    }
    List<BaseComponent> components =
        new ArrayList<>(Arrays.asList(Components.getComponent(copy.getRaw())));
    copy.getExtra().forEach(line -> components.addAll(Arrays.asList(line.build(sample))));
    return components.toArray(new BaseComponent[0]);
  }

  /**
   * Build the message.
   *
   * @param channel the channel to build the message for
   * @param placeholders whether to append placeholders
   * @param sample whether the line must be formatted using {@link
   *     me.googas.chat.api.lines.format.SampleFormatter}
   * @return the built message
   */
  default BaseComponent[] build(@NonNull Channel channel, boolean placeholders, boolean sample) {
    Line copy = this.copy();
    if (placeholders) {
      copy.setRaw(PlaceholderManager.getInstance().build(channel, copy.getRaw()));
    }
    if (sample) {
      ResourceManager.getInstance()
          .getSampleFormatter()
          .format(channel.getLocale().orElseGet(ResourceManager::getBase), copy);
    }
    List<BaseComponent> components =
        new ArrayList<>(Arrays.asList(Components.getComponent(copy.getRaw())));
    copy.getExtra()
        .forEach(
            line -> components.addAll(Arrays.asList(line.build(channel, placeholders, sample))));
    return components.toArray(new BaseComponent[0]);
  }

  /**
   * Build the message.
   *
   * @param channel the channel to build the message for
   * @return the built message
   */
  default BaseComponent[] build(@NonNull Channel channel) {
    return this.build(channel, true, true);
  }

  default void send(@NonNull Channel channel) {
    channel.send(this);
  }

  default void send(@NonNull Channel channel, boolean placeholders, boolean sample) {
    channel.send(this.build(channel, placeholders, sample));
  }

  /**
   * Set the raw text of the line.
   *
   * @see #getRaw()
   * @param raw the new raw text
   * @return this same instance
   */
  @NonNull
  Line setRaw(@NonNull String raw);

  /**
   * Format the message using an array of objects.
   *
   * @param objects the objects to format the message
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Object... objects);

  @Override
  @NonNull
  default List<BaseComponent> getComponents() {
    return Arrays.asList(this.build());
  }

  /**
   * Format the message using a formatter.
   *
   * @param formatter the formatter to format the message
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Formatter formatter);

  /**
   * Append a line.
   *
   * @param line the line to append
   * @return this same instance
   */
  @NonNull
  Line append(@NonNull Line line);

  @NonNull
  Line format(@NonNull Line.Placeholder placeholder);

  @NonNull
  default Line placeholder(@NonNull String key, Object value, @NonNull String def) {
    return this.format(new Line.Placeholder(key, value, def));
  }

  @NonNull
  default Line placeholder(@NonNull String key, Object value) {
    return this.format(new Line.Placeholder(key, value));
  }

  @NonNull
  default Line placeholders(@NonNull Placeholder... placeholders) {
    for (Placeholder placeholder : placeholders) {
      this.format(placeholder);
    }
    return this;
  }

  /**
   * Append a string. This will use {@link #of(String)} which means it will append a plain line
   *
   * @param string the string to append
   * @return this same instance
   */
  @NonNull
  default Line append(@NonNull String string) {
    return this.append(Line.of(string));
  }

  /**
   * Get the raw text of the line. This is the line without being formatted.
   *
   * <p>Ex: {@link Localized} the raw text is its json
   *
   * @return the raw text
   */
  @NonNull
  String getRaw();

  /**
   * Get this line as a {@link ArgumentProviderException}.
   *
   * @return the new {@link ArgumentProviderException}
   */
  @NonNull
  default ArgumentProviderException asProviderException() {
    return new ArgumentProviderException(this.asText());
  }

  /**
   * Build the message as text.
   *
   * @param channel the channel to build the message for
   * @param placeholders whether to append placeholders
   * @param sample whether the line must be formatted using {@link
   *     me.googas.chat.api.lines.format.SampleFormatter}
   * @return the built message as text
   */
  @NonNull
  default String asText(@NonNull Channel channel, boolean placeholders, boolean sample) {
    Line copy = this.copy();
    if (placeholders) {
      copy.setRaw(PlaceholderManager.getInstance().build(channel, copy.getRaw()));
    }
    if (sample) {
      ResourceManager.getInstance()
          .getSampleFormatter()
          .format(channel.getLocale().orElseGet(ResourceManager::getBase), copy);
    }
    if (Line.isJson(copy.getRaw())) {
      return new TextComponent(copy.build(channel, placeholders, sample)).toLegacyText();
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(copy.getRaw()));
      copy.getExtra().stream()
          .map(line -> line.asText(channel, placeholders, sample))
          .forEach(builder::append);
      return builder.toString();
    }
  }

  /**
   * Build the message as text.
   *
   * @param channel the channel to build the message for
   * @return the built message as text
   */
  @NonNull
  default String asText(@NonNull Channel channel) {
    return this.asText(channel, true, true);
  }

  /**
   * Build the message as text.
   *
   * @param sample whether the line must be formatted using {@link
   *     me.googas.chat.api.lines.format.SampleFormatter}
   * @return the built message as text
   */
  @NonNull
  default String asText(boolean sample) {
    Line copy = this.copy();
    if (sample) {
      ResourceManager.getInstance().getSampleFormatter().format(ResourceManager.getBase(), copy);
    }
    if (Line.isJson(copy.getRaw())) {
      return new TextComponent(copy.build()).toLegacyText();
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(copy.getRaw()));
      copy.getExtra().stream().map(line -> line.asText(sample)).forEach(builder::append);
      return builder.toString();
    }
  }

  /**
   * Build the message as text.
   *
   * @return the built message as text
   */
  @NonNull
  default String asText() {
    return this.asText(true);
  }

  /**
   * Get the extra lines that have been appended
   *
   * @return the extra lines in a list
   */
  @NonNull
  Collection<Line> getExtra();

  @NonNull
  default Line appendMany(@NonNull Collection<Line> extra) {
    extra.forEach(this::append);
    return this;
  }

  @NonNull
  default Line appendMany(@NonNull Line... lines) {
    return this.appendMany(Arrays.asList(lines));
  }

  static boolean isJson(@NonNull String string) {
    return string.startsWith("{") && string.endsWith("}")
        || string.startsWith("[") && string.endsWith("]");
  }

  final class Placeholder {

    @NonNull private final String key;
    private final Object value;
    @NonNull private final String def;

    public Placeholder(@NonNull String key, Object value, @NonNull String def) {
      this.key = key;
      this.value = value;
      this.def = def;
    }

    public Placeholder(@NonNull String key, Object value) {
      this(key, value, "null");
    }

    @NonNull
    public String getKey() {
      return this.key;
    }

    @NonNull
    public String getValue() {
      return this.value == null ? this.def : this.value.toString();
    }

    @NonNull
    public String format(String text) {
      return text.replace("%" + key + "%", this.getValue());
    }
  }
}
