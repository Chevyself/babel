package me.googas.chat.api.lines;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.ForwardingChannel;
import me.googas.chat.api.Language;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.chat.api.placeholders.PlaceholderManager;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
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
    return Line.localized(channel.getLocale().orElse(Locale.ENGLISH), key);
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
        string = string.substring(1);
      }
      return Line.localized(locale, string);
    } else {
      return Line.of(string);
    }
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
  BaseComponent[] build();

  /**
   * Build this line with placeholders. The placeholders will be built using {@link
   * PlaceholderManager}
   *
   * @param player the player to build the placeholders
   * @return the built {@link BaseComponent}
   */
  @NonNull
  default BaseComponent[] buildWithPlaceholders(@NonNull OfflinePlayer player) {
    Line copy = this.copy();
    return copy.setRaw(PlaceholderManager.getInstance().build(player, copy.getRaw())).build();
  }

  /**
   * Build this line with placeholders as {@link String}. The placeholders will be built using
   * {@link PlaceholderManager}
   *
   * @param player the player to build the placeholders
   * @return the built {@link String}
   */
  @NonNull
  default Optional<String> asTextWithPlaceholders(@NonNull OfflinePlayer player) {
    Line copy = this.copy();
    return copy.setRaw(PlaceholderManager.getInstance().build(player, copy.getRaw())).asText();
  }

  /**
   * Send this line to a {@link Channel}.
   *
   * @see #sendWithPlaceholders(Channel)
   * @param channel the channel to send this line to
   * @param placeholders whether to build this line with placeholders
   */
  default void send(@NonNull Channel channel, boolean placeholders) {
    if (channel instanceof PlayerChannel && placeholders) {
      channel.send(this.buildWithPlaceholders(((PlayerChannel) channel).getOffline()));
    } else {
      this.send(channel);
    }
  }

  /**
   * Send this line to a {@link Channel}.
   *
   * @param channel the channel to send this line to
   */
  default void send(@NonNull Channel channel) {
    channel.send(this.build());
  }

  /**
   * Send this line with placeholders.
   *
   * @param channel the channel to send this line to
   */
  default void sendWithPlaceholders(@NonNull Channel channel) {
    if (channel instanceof PlayerChannel) {
      this.send(channel, true);
    } else {
      this.send(channel);
    }
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
   * Format the message using a map of placeholders.
   *
   * @param map the map of the placeholders
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Map<String, String> map);

  /**
   * Format the message using a formatter.
   *
   * @param formatter the formatter to format the message
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Formatter formatter);

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
   * This must be used if the line is a sample line to format it. This line will be formatted using
   * {@link me.googas.chat.api.lines.format.SampleFormatter}
   *
   * @return this same instance
   */
  @NonNull
  default Line formatSample() {
    this.format(ResourceManager.getInstance().getSampleFormatter());
    return this;
  }

  /**
   * Get this line as a {@link ArgumentProviderException}.
   *
   * @return the new {@link ArgumentProviderException}
   */
  @NonNull
  default ArgumentProviderException asProviderException() {
    if (this.asText().isPresent()) {
      return new ArgumentProviderException(this.asText().get());
    }
    return new ArgumentProviderException();
  }

  /**
   * Format this sample using a locale.
   *
   * @param locale the locale to format this sample with
   * @return this line formatted
   */
  @NonNull
  default Line formatSample(@NonNull Locale locale) {
    ResourceManager.getInstance().getSampleFormatter().format(locale, this);
    return this;
  }

  /**
   * Build the message as text.
   *
   * @return the built message as text
   */
  @NonNull
  Optional<String> asText();
}
