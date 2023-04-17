package com.github.chevyself.babel.api.dependencies.papi;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

/** A class to build placeholders using PlaceholderAPI. */
public final class PapiPlaceholderBuilder {

  /**
   * Change the placeholders of a raw string with text.
   *
   * @param player the player to build the placeholders
   * @param raw the raw string of text
   * @return the built string
   */
  @NonNull
  public String build(@NonNull OfflinePlayer player, @NonNull String raw) {
    return PlaceholderAPI.setPlaceholders(player, raw);
  }
}
