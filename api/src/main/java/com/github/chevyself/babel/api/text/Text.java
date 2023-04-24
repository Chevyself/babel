package com.github.chevyself.babel.api.text;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.channels.ConsoleChannel;
import com.github.chevyself.babel.api.channels.ForwardingChannel;
import com.github.chevyself.babel.api.lang.Language;
import com.github.chevyself.babel.api.placeholders.PlaceholderManager;
import com.github.chevyself.babel.api.text.format.Formatter;
import com.github.chevyself.babel.api.text.format.SampleFormatter;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.bukkit.utils.Components;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

/**
 * Represents text that can be displayed to the player.
 *
 * <p>Text can either be plain, or a reference to localized text. Localized text is a key that can
 * be translated into different languages depending on the client's locale.
 *
 * <p>Plain and Localized text are mutable, meaning that their contents can be modified using
 * methods such as {#setraw(String)} or any of the formatting methods.
 *
 * <p>However, they can also be safely copied using {@link #copy()}.
 *
 * <p>To ensure thread safety, each thread should use its own instance of text, however, {@link
 * LocalizedTextReference} is immutable and can be used in multiple threads.
 */
public interface Text extends BukkitResult {

  /**
   * Start localized text.
   *
   * @param locale the locale to get the language
   * @param key the key to get the json/text message
   * @return a new {@link LocalizedText} instance
   * @throws NullPointerException if the locale or key is null
   */
  @NonNull
  static LocalizedText localized(@NonNull Locale locale, @NonNull String key) {
    return new LocalizedText(locale, ResourceManager.getInstance().getRaw(locale, key).trim());
  }

  /**
   * Start localized text.
   *
   * @param sender the sender to get the language
   * @param key the key to get the json/text message
   * @return a new {@link LocalizedText} instance
   * @throws NullPointerException if the sender or key is null
   */
  @NonNull
  static LocalizedText localized(@NonNull CommandSender sender, @NonNull String key) {
    return Text.localized(Language.getLocale(sender), key);
  }

  /**
   * Start localized text.
   *
   * @param channel the channel to get the language
   * @param key the key to get the json/text message
   * @return a new {@link LocalizedText} instance
   * @throws NullPointerException if the channel or key is null
   */
  @NonNull
  static LocalizedText localized(@NonNull Channel channel, String key) {
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
  static List<LocalizedText> localized(
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
  static PlainText of(@NonNull String text) {
    return new PlainText(text);
  }

  /**
   * Get a localized reference from a key.
   *
   * @param key the key of the localized message
   * @return ta new {@link LocalizedTextReference} instance
   * @throws NullPointerException if the key is null
   */
  @NonNull
  static LocalizedTextReference localized(@NonNull String key) {
    return new LocalizedTextReference(key);
  }

  /**
   * Parses text from a string. If the string starts with 'localized:' or '$' a {@link
   * LocalizedTextReference} will be returned, else a {@link PlainText} will be provided/
   *
   * <p>If the string starts with 'localized:', the key will be extracted from the string and a
   * {@link LocalizedTextReference} will be created using it. If the string starts with '$', the
   * dollar sign will be removed from the string and a new {@link LocalizedTextReference} will be
   * created using the resulting string.
   *
   * <p>Otherwise, a {@link PlainText} will be created using the string
   *
   * @param string the string to parse
   * @return the parsed text
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static Text parse(@NonNull String string) {
    if (TextUtils.isLocalized(string)) {
      return Text.localized(TextUtils.extractKey(string));
    }
    return Text.of(string);
  }

  /**
   * Parses text from a string, returning a {@link LocalizedText} instance if the string starts with
   * 'localized:', otherwise a {@link PlainText} instance will be returned.
   *
   * @param locale the locale parsing the text
   * @param string the string to parse
   * @return the parsed text
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static Text parse(Locale locale, @NonNull String string) {
    if (TextUtils.isLocalized(string) && locale != null) {
      return Text.localized(locale, TextUtils.extractKey(string));
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
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static Text parse(Channel channel, @NonNull String string) {
    return Text.parse(
        channel == null ? null : channel.getLocale().orElse(ResourceManager.getBase()), string);
  }

  /**
   * Check whether this text is a sample. If the text is sample it will be formatted using {@link
   * SampleFormatter}.
   *
   * <p>A sample text is one which contains references to other texts. For example, if you have a
   * text with the content "Hello ${owner.name}" it is referencing as a {@link
   * LocalizedTextReference} to the text with the key "owner.name".
   *
   * @return true if this text is a sample
   */
  boolean isSample();

  /**
   * Set whether this text is a sample.
   *
   * @see #isSample()
   * @param sample whether this text is a sample
   * @return this instance
   */
  @NonNull
  Text setSample(boolean sample);

  /**
   * Check whether this text has placeholders. If the text has placeholders it will be formatted
   * using the {@link PlaceholderManager}.
   *
   * <p>A text has placeholders if it contains a placeholder. For example, if you have a text with
   * the content "Hello %owner.name%" it has a placeholder.
   *
   * @return true if this text has placeholders
   */
  boolean hasPlaceholders();

  /**
   * Set whether this text has placeholders.
   *
   * @see #hasPlaceholders()
   * @param placeholders whether this text has placeholders
   * @return this instance
   */
  @NonNull
  Text setHasPlaceholders(boolean placeholders);

  /**
   * Creates a copy of this text.
   *
   * @return a new copied instance of this text
   */
  @NonNull
  Text copy();

  /**
   * Build the text as an array of {@link BaseComponent}.
   *
   * @return the built array
   */
  @NonNull
  default BaseComponent[] build() {
    return this.build(ConsoleChannel.getInstance());
  }

  /**
   * Build the message.
   *
   * @param channel the channel to build the message for {@link SampleFormatter}
   * @return the built message
   */
  default BaseComponent[] build(@NonNull Channel channel) {
    Text copy = this.copy();
    if (this.hasPlaceholders()) {
      TextUtils.formatPlaceholders(channel, copy);
    }
    if (this.isSample()) {
      TextUtils.formatSample(channel.getLocale().orElseGet(ResourceManager::getBase), copy);
    }
    return Stream.concat(
            Stream.of(Components.getComponent(copy.getRaw())),
            copy.getExtra().stream().flatMap(line -> Arrays.stream(line.build(channel))))
        .toArray(BaseComponent[]::new);
  }

  default void send(@NonNull Channel channel) {
    channel.send(this);
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
   * <p>Ex: {@link LocalizedText} the raw text is its json
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
    return new ArgumentProviderException(this.asString());
  }

  /**
   * Build the text as string.
   *
   * @param channel the channel to build the message for
   * @return the built message as text
   */
  @NonNull
  default String asString(@NonNull Channel channel) {
    Text copy = this.copy();
    if (this.hasPlaceholders()) {
      TextUtils.formatPlaceholders(channel, copy);
    }
    if (this.isSample()) {
      TextUtils.formatSample(channel.getLocale().orElseGet(ResourceManager::getBase), copy);
    }
    if (TextUtils.isJson(copy.getRaw())) {
      return new TextComponent(copy.build(channel)).toLegacyText();
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(copy.getRaw()));
      copy.getExtra().stream().map(text -> text.asString(channel)).forEach(builder::append);
      return builder.toString();
    }
  }

  /**
   * Build the message as string.
   *
   * @return the built message as text
   */
  @NonNull
  default String asString() {
    return this.asString(ConsoleChannel.getInstance());
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

  /**
   * Represents a placeholder which may be used in a {@link Text} to replace a key with a value.
   * Placeholders are identified with a key inside percent signs. For example, if the key is "name"
   * then the placeholder will be "%name%".
   */
  final class Placeholder {

    @NonNull private final String key;
    private final Object value;
    @NonNull private final String def;

    /**
     * Create the text placeholder.
     *
     * @param key the key to identify the placeholder
     * @param value the value to replace the placeholder
     * @param def the default value to replace the placeholder if the value is null
     * @throws NullPointerException if the key or the default value is null
     */
    public Placeholder(@NonNull String key, Object value, @NonNull String def) {
      this.key = key;
      this.value = value;
      this.def = def;
    }

    /**
     * Create the text placeholder. The default value will be the literal "null"
     *
     * @param key the key to identify the placeholder
     * @param value the value to replace the placeholder
     * @throws NullPointerException if the key is null
     */
    public Placeholder(@NonNull String key, Object value) {
      this(key, value, "null");
    }

    /**
     * Get the key of the placeholder
     *
     * @return the key
     */
    @NonNull
    public String getKey() {
      return this.key;
    }

    /**
     * Get the value of the placeholder
     *
     * @return the value
     */
    @NonNull
    public String getValue() {
      return this.value == null ? this.def : this.value.toString();
    }

    /**
     * Formats a string using the placeholder. This will replace the key with the value, for
     * instance:
     *
     * <pre>
     *     Placeholder placeholder = new Placeholder("name", "John");
     *     String formatted = placeholder.format("Hello %name%");
     *     System.out.println(formatted); // Hello John
     * </pre>
     *
     * @param text the text to format
     * @return the formatted text
     * @throws NullPointerException if the text is null
     */
    @NonNull
    public String format(@NonNull String text) {
      return text.replace("%" + key + "%", this.getValue());
    }
  }
}
