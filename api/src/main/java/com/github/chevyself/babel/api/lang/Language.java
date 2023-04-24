package com.github.chevyself.babel.api.lang;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.util.Players;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a language.
 *
 * <p>This is used to get the actual message of localized text: {@link
 * com.github.chevyself.babel.api.text.LocalizedText}. Using the different methods you could get
 * either an array of components or a string.
 *
 * <ul>
 *   <li>{@link #getRaw(String)}: get the raw string of a key
 *   <li>{@link #get(String)}: get the localized text of a key
 *   <li>{@link #get(String, Map)}: get the localized text of a key with a map of placeholders
 *   <li>{@link #get(String, Object...)}: get the localized text of a key with a list of
 *       placeholders
 * </ul>
 *
 * <p>Languages are registered in {@link ResourceManager}.
 */
public interface Language {

  /**
   * Get the locale from a {@link CommandSender}.
   *
   * @param sender the sender to get the locale
   * @return the locale of the sender
   */
  @NonNull
  static Locale getLocale(@NonNull CommandSender sender) {
    if (sender instanceof Player) {
      return Language.getLocale(Players.getLocale((Player) sender));
    } else {
      return ResourceManager.getBase();
    }
  }

  /**
   * Get the locale from a {@link OfflinePlayer}.
   *
   * @param player the player to get the locale from
   * @return the locale
   */
  static Locale getOfflineLocale(@NonNull OfflinePlayer player) {
    Player online = player.getPlayer();
    return online != null ? Language.getLocale(online) : ResourceManager.getBase();
  }

  /**
   * Get the raw string of a key.
   *
   * @param key the key to get the raw string from
   * @return the raw string
   */
  @NonNull
  Optional<String> getRaw(@NonNull String key);

  /**
   * Get the base component of a key.
   *
   * <p>This parses the raw string, using: {@link
   * com.github.chevyself.starbox.bukkit.utils.Components#getComponent(String)}
   *
   * @param key the key to get the base component from
   * @return the base component
   */
  @NonNull
  BaseComponent[] get(@NonNull String key);

  /**
   * Get the base component of a key with a map of placeholders.
   *
   * <p>Before parsing, the raw string is formatted using {@link
   * com.github.chevyself.starbox.util.Strings#format(String, Map)}
   *
   * @see #get(String)
   * @param key the key to get the base component from
   * @param map the map of placeholders
   * @return the base component
   */
  @NonNull
  BaseComponent[] get(@NonNull String key, @NonNull Map<String, String> map);

  /**
   * Get the base component of a key with a list of placeholders.
   *
   * <p>Before parsing, the raw string is formatted using {@link
   * com.github.chevyself.starbox.util.Strings#format(String, Object...)}
   *
   * @see #get(String)
   * @param key the key to get the base component from
   * @param objects the list of placeholders
   * @return the base component
   */
  @NonNull
  BaseComponent[] get(@NonNull String key, Object... objects);

  /**
   * Whether the language represents a sample language.
   *
   * @return true if the language contains sample keys
   */
  boolean isSample();

  /**
   * Get the locale to which objects will be based to.
   *
   * @return the locale
   */
  @NonNull
  Locale getLocale();

  /**
   * Get a locale from a string such as 'en_US'.
   *
   * @param locale the string to get the locale from
   * @return the locale
   */
  @NonNull
  static Locale getLocale(@NonNull String locale) {
    String[] split = locale.split("_", 3);
    switch (split.length) {
      case 1:
        return new Locale(split[0]);
      case 2:
        return new Locale(split[0], split[1]);
      case 3:
        return new Locale(split[0], split[1], split[2]);
      default:
        throw new IllegalArgumentException(locale + " does not match a locale");
    }
  }
}
