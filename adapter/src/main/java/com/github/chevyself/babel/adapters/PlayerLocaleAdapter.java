package com.github.chevyself.babel.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

/** An adapter to get the locale of a player's client. */
public interface PlayerLocaleAdapter {

  /**
   * Get the locale of the player's client.
   *
   * @param player the player to get the locale
   * @return the locale of the player's client
   */
  @NonNull
  String getLocale(@NonNull Player player);
}
