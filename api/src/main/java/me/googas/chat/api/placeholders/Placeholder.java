package me.googas.chat.api.placeholders;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;

/**
 * Represents a placeholder. A placeholder in a raw string is shown inside percentages:
 * %name%, this replaces that with text
 */
public interface Placeholder {

  /**
   * Check whether this placeholder applies to the given name.
   *
   * @param name the name to check
   * @return true if this placeholder applies for the name
   * @throws NullPointerException if the name is null
   */
  boolean hasName(@NonNull String name);

  /**
   * Build the placeholder.
   *
   * @param key the key/name that was used in {@link #hasName(String)}
   * @param player the player to build the placeholder to
   * @return the built placeholder
   * @throws NullPointerException if the key or player is null
   */
  @NonNull
  String build(@NonNull String key, @NonNull OfflinePlayer player);
}
