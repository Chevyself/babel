package me.googas.chat.api.text;

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
import me.googas.chat.api.text.format.Formatter;
import me.googas.chat.api.placeholders.PlaceholderManager;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bukkit.utils.Components;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

/**
 * Represents text that can be displayed to the player.
 *
 * <p>Text can either be plain, or a reference to localized text. Localized text is a key
 * that can be translated into different languages depending on the client's locale.
 *
 * <p>Plain and Localized lines are mutable, meaning that their contents can be modified using
 * methods such as {#setraw(String)} or any of the formatting methods.
 *
 * <p>However, they can also be safely copied using {@link #copy()}.
 *
 * <p>To ensure thread safety, each thread should use its own instance of text, however, {@link
 * LocalizedReference} is immutable and can be used in multiple threads.
 */
public interface Text extends BukkitResult {

  /**
   * Start localized text.
   *
   * @param locale the locale to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   * @throws NullPointerException if the locale or key is null
   */
  @NonNull
  static Localized localized(@NonNull Locale locale, @NonNull String key) {
    return new Localized(locale, ResourceManager.getInstance().getRaw(locale, key).trim());
  }

  /**
   * Start localized text.
   *
   * @param sender the sender to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   * @throws NullPointerException if the sender or key is null
   */
  @NonNull
  static Localized localized(@NonNull CommandSender sender, @NonNull String key) {
    return Text.localized(Language.getLocale(sender), key);
  }

  /**
   * Start localized text.
   *
   * @param channel the channel to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   * @throws NullPointerException if the channel or key is null
   */
  @NonNull
  static Localized localized(@NonNull Channel channel, String key) {
    return Text.localized(channel.getLocale().orElse(ResourceManager.getBase()), key);
  }

  /**
   * Get the localized lines for a forwarding channel.
   *
   * @param forwardingChannel the forwarding channel to get the lines
   * @param key the key to get the json/text message
   * @return a {@link List} containing the lines
   * @throws NullPointerException if the forwarding channel or key is null
   */
  @NonNull
  static List<Localized> localized(
      @NonNull ForwardingChannel.Multiple forwardingChannel, @NonNull String key) {
    return forwardingChannel.getChannels().stream()
        .map(channel -> Text.localized(channel, key))
        .collect(Collectors.toList());
  }

  /**
   * Start plain text.
   *
   * @param text the text to convert to plain
   * @return plain text
   * @throws NullPointerException if the text is null
   */
  @NonNull
  static Plain of(@NonNull String text) {
    return new Plain(text);
  }

  /**
   * Get a localized reference from a key.
   *
   * @param key the key of the localized message
   * @return ta new {@link LocalizedReference} instance
   * @throws NullPointerException if the key is null
   */
  @NonNull
  static LocalizedReference localized(@NonNull String key) {
    return new LocalizedReference(key);
  }

  /**
   * Extracts the key from a string. If the prefix is '$', it will remove the prefix and return the
   * rest of the string, otherwise it will remove the prefix 'localized' and the colon.
   *
   * @param string the string to extract the key from
   * @return the extracted key
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static String extractKey(@NonNull String string) {
    String prefix = string.startsWith("localized:") ? "localized:" : "$";
    if (prefix.equals("$")) {
      if (string.startsWith("${") && string.endsWith("}")) {
        return string.substring(2, string.length() - 1);
      } else {
        return string.substring(1);
      }
    } else {
      return string.substring(10);
    }
  }

  /**
   * Checks whether a string is localized text. A string is localized text if it starts with
   * 'localized:' or '$' and it does not contain any spaces.
   *
   * @param string the string to check
   * @return true if the string is localized text
   * @throws NullPointerException if the string is null
   */
  static boolean isLocalized(@NonNull String string) {
    return !string.contains(" ") && (string.startsWith("localized:") || string.startsWith("$"));
  }

  /**
   * Parses text from a string. If the string starts with 'localized:' or '$' a {@link
   * LocalizedReference} will be returned, else a {@link Plain} will be provided/
   *
   * <p>If the string starts with 'localized:', the key will be extracted from the string and a
   * {@link LocalizedReference} will be created using it. If the string starts with '$', the dollar
   * sign will be removed from the string and a new {@link LocalizedReference} will be created using
   * the resulting string.
   *
   * <p>Otherwise, a {@link Plain} will be created using the string
   *
   * <p>By default all Lines are treated as "samples" which means that the raw content of the text
   * will be treated as if it had references to other lines. If you want to disable this behaviour,
   * you can use {@link #build(boolean)} or {@link #build(Channel, boolean, boolean)}.
   *
   * <p>By default all lines will be formatted using placeholders in the {@link PlaceholderManager}.
   * If you want to disable this behaviour, you can use {@link #build(Channel, boolean, boolean)}.
   *
   * @param string the string to parse
   * @return the parsed text
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static Text parse(@NonNull String string) {
    if (Text.isLocalized(string)) {
      return Text.localized(Text.extractKey(string));
    }
    return Text.of(string);
  }

  /**
   * Parses text from a string, returning a {@link Localized} instance if the string starts with
   * 'localized:', otherwise a {@link Plain} instance will be returned.
   *
   * @param locale the locale parsing the text
   * @param string the string to parse
   * @return the parsed text
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static Text parse(Locale locale, @NonNull String string) {
    if (Text.isLocalized(string) && locale != null) {
      return Text.localized(locale, Text.extractKey(string));
    } else {
      return Text.of(string);
    }
  }

  /**
   * Parse text from a string.
   *
   * @see #parse(Locale, String)
   * @param channel to get the locale from
   * @param string the string to parse
   * @return the parsed text
   * @throws NullPointerException if the channel or string is null
   */
  @NonNull
  static Text parse(Channel channel, @NonNull String string) {
    return parse(
        channel == null ? null : channel.getLocale().orElse(ResourceManager.getBase()), string);
  }

  /**
   * Creates a copy of this text.
   *
   * @return a new copied instance of this text
   */
  @NonNull
  Text copy();

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
   * Build the text as an array of {@link BaseComponent}.
   *
   * @param sample this text is a sample and should be formatted accordingly using {@link
   *     me.googas.chat.api.text.format.SampleFormatter}
   * @return the built array
   */
  @NonNull
  default BaseComponent[] build(boolean sample) {
    Text copy = this.copy();
    if (sample) {
      ResourceManager.getInstance().getSampleFormatter().format(ResourceManager.getBase(), copy);
    }
    List<BaseComponent> components =
        new ArrayList<>(Arrays.asList(Components.getComponent(copy.getRaw())));
    copy.getExtra().forEach(text -> components.addAll(Arrays.asList(text.build(sample))));
    return components.toArray(new BaseComponent[0]);
  }

  /**
   * Build the message.
   *
   * @param channel the channel to build the message for
   * @param placeholders whether to append placeholders
   * @param sample whether the text must be formatted using {@link
   *     me.googas.chat.api.text.format.SampleFormatter}
   * @return the built message
   */
  default BaseComponent[] build(@NonNull Channel channel, boolean placeholders, boolean sample) {
    Text copy = this.copy();
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
            text -> components.addAll(Arrays.asList(text.build(channel, placeholders, sample))));
    return components.toArray(new BaseComponent[0]);
  }

  default BaseComponent[] build(@NonNull Channel channel, boolean sample) {
    return this.build(channel, true, sample);
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
   * Format the message using an array of objects.
   *
   * @param objects the objects to format the message
   * @return this same instance
   */
  @NonNull
  Text format(@NonNull Object... objects);

  /**
   * Format the message using a formatter.
   *
   * @param formatter the formatter to format the message
   * @return this same instance
   */
  @NonNull
  Text format(@NonNull Formatter formatter);

  @Override
  @NonNull
  default List<BaseComponent> getComponents() {
    return Arrays.asList(this.build());
  }

  /**
   * Append text.
   *
   * @param text the text to append
   * @return this same instance
   */
  @NonNull
  Text append(@NonNull Text text);

  @NonNull
  Text format(@NonNull Text.Placeholder placeholder);

  @NonNull
  default Text placeholder(@NonNull String key, Object value, @NonNull String def) {
    return this.format(new Text.Placeholder(key, value, def));
  }

  @NonNull
  default Text placeholder(@NonNull String key, Object value) {
    return this.format(new Text.Placeholder(key, value));
  }

  @NonNull
  default Text placeholders(@NonNull Placeholder... placeholders) {
    for (Placeholder placeholder : placeholders) {
      this.format(placeholder);
    }
    return this;
  }

  /**
   * Append a string. This will use {@link #of(String)} which means it will append plain text
   *
   * @param string the string to append
   * @return this same instance
   */
  @NonNull
  default Text append(@NonNull String string) {
    return this.append(Text.of(string));
  }

  /**
   * Get the raw text. This is the text without being formatted.
   *
   * <p>Ex: {@link Localized} the raw text is its json
   *
   * @return the raw text
   */
  @NonNull
  String getRaw();

  /**
   * Set the raw text.
   *
   * @see #getRaw()
   * @param raw the new raw text
   * @return this same instance
   */
  @NonNull
  Text setRaw(@NonNull String raw);

  /**
   * Get this text as a {@link ArgumentProviderException}.
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
   * @param sample whether the text must be formatted using {@link
   *     me.googas.chat.api.text.format.SampleFormatter}
   * @return the built message as text
   */
  @NonNull
  default String asText(@NonNull Channel channel, boolean placeholders, boolean sample) {
    Text copy = this.copy();
    if (placeholders) {
      copy.setRaw(PlaceholderManager.getInstance().build(channel, copy.getRaw()));
    }
    if (sample) {
      ResourceManager.getInstance()
          .getSampleFormatter()
          .format(channel.getLocale().orElseGet(ResourceManager::getBase), copy);
    }
    if (Text.isJson(copy.getRaw())) {
      return new TextComponent(copy.build(channel, placeholders, sample)).toLegacyText();
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(copy.getRaw()));
      copy.getExtra().stream()
          .map(text -> text.asText(channel, placeholders, sample))
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
   * @param sample whether the text must be formatted using {@link
   *     me.googas.chat.api.text.format.SampleFormatter}
   * @return the built message as text
   */
  @NonNull
  default String asText(boolean sample) {
    Text copy = this.copy();
    if (sample) {
      ResourceManager.getInstance().getSampleFormatter().format(ResourceManager.getBase(), copy);
    }
    if (Text.isJson(copy.getRaw())) {
      return new TextComponent(copy.build()).toLegacyText();
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(copy.getRaw()));
      copy.getExtra().stream().map(text -> text.asText(sample)).forEach(builder::append);
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
  Collection<Text> getExtra();

  @NonNull
  default Text appendMany(@NonNull Collection<Text> extra) {
    extra.forEach(this::append);
    return this;
  }

  @NonNull
  default Text appendMany(@NonNull Text... texts) {
    return this.appendMany(Arrays.asList(texts));
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
