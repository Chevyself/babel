package me.googas.chat.api.placeholders;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.dependencies.papi.PapiSoft;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

/**
 * This class manages all the placeholders.
 *
 * <p>Placeholders must be registered using a plugin.
 *
 * <p>It does support PAPI placeholders.
 */
public final class PlaceholderManager {

  @NonNull @Getter public static final PlaceholderManager instance = new PlaceholderManager();
  @NonNull private static final Pattern PATTERN = Pattern.compile("%.*?%");

  @NonNull private final Map<Plugin, Set<Placeholder>> map = new HashMap<>();

  /**
   * Register a {@link Placeholder}.
   *
   * @param plugin the plugin that is registering the placeholder
   * @param placeholder the placeholder that is being registered
   * @return this same instance
   * @throws NullPointerException if the plugin or placeholder is null
   */
  @NonNull
  public PlaceholderManager register(@NonNull Plugin plugin, @NonNull Placeholder placeholder) {
    map.computeIfAbsent(plugin, key -> new HashSet<>()).add(placeholder);
    return this;
  }

  /**
   * Register many {@link Placeholder}.
   *
   * @param plugin the plugin that is registering the placeholders
   * @param placeholders the placeholders that are being registered
   * @return this same instance
   * @throws NullPointerException if the plugin or placeholders is null
   */
  @NonNull
  public PlaceholderManager registerAll(
      @NonNull Plugin plugin, @NonNull Collection<? extends Placeholder> placeholders) {
    this.map.computeIfAbsent(plugin, pluginKey -> new HashSet<>()).addAll(placeholders);
    return this;
  }

  /**
   * Register many {@link Placeholder}.
   *
   * @param plugin the plugin that is registering the placeholders
   * @param placeholders the placeholders that are being registered
   * @return this same instance
   * @throws NullPointerException if the plugin or placeholders is null
   */
  @NonNull
  public PlaceholderManager registerAll(
      @NonNull Plugin plugin, @NonNull Placeholder... placeholders) {
    return this.registerAll(plugin, Arrays.asList(placeholders));
  }

  /**
   * Unregisters all placeholders from a {@link Plugin}.
   *
   * @param plugin the plugin to unregister the placeholders
   * @return this same instance
   * @throws NullPointerException if the plugin is null
   */
  @NonNull
  public PlaceholderManager unregister(@NonNull Plugin plugin) {
    this.map.remove(plugin);
    return this;
  }

  /**
   * Get a placeholder by its name.
   *
   * @param name the name of the placeholder to get
   * @return the placeholder matching the name, might be null
   * @throws NullPointerException if the name is null
   */
  public Placeholder getPlaceholder(@NonNull String name) {
    for (Set<Placeholder> placeholders : map.values()) {
      for (Placeholder placeholder : placeholders) {
        if (placeholder.hasName(name)) {
          return placeholder;
        }
      }
    }
    return null;
  }

  /**
   * Build a {@link String} with placeholders for a player.
   *
   * @param player the player to build the string for
   * @param raw the raw string to build
   * @return the built string
   * @throws NullPointerException if the player or raw text is null
   */
  @NonNull
  public String build(@NonNull OfflinePlayer player, @NonNull String raw) {
    if (PapiSoft.isEnabled()) {
      raw = PapiSoft.getBuilder().build(player, raw);
    }
    Matcher matcher = PlaceholderManager.PATTERN.matcher(raw);
    while (matcher.find()) {
      String name = matcher.group().replace("%", "");
      Placeholder placeholder = this.getPlaceholder(name);
      if (placeholder != null) raw = raw.replace("%" + name + "%", placeholder.build(name, player));
    }
    return raw;
  }

  /**
   * Build a {@link String} with placeholders for a channel.
   *
   * @param channel the channel to build the string for
   * @param raw the raw string to build
   * @return the built string
   * @throws NullPointerException if the channel or raw text is null
   */
  @NonNull
  public String build(@NonNull Channel channel, @NonNull String raw) {
    if (channel instanceof PlayerChannel) {
      return this.build(((PlayerChannel) channel).getOffline(), raw);
    }
    return raw;
  }
}
