package me.googas.chat.api;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.chat.api.util.Players;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
      return Locale.ENGLISH;
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
    return online != null ? Language.getLocale(online) : Locale.ENGLISH;
  }

  @NonNull
  Optional<String> getRaw(@NonNull String key);

  @NonNull
  BaseComponent[] get(@NonNull String key);

  @NonNull
  BaseComponent[] get(@NonNull String key, @NonNull Map<String, String> map);

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
