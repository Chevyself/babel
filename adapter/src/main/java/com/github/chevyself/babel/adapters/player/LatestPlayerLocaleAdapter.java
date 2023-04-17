package com.github.chevyself.babel.adapters.player;

import com.github.chevyself.babel.adapters.PlayerLocaleAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** This adapter uses the latest method to get the locale of a player. */
public final class LatestPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.getLocale();
  }
}
