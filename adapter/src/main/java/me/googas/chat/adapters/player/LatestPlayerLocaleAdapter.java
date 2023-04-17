package me.googas.chat.adapters.player;

import lombok.NonNull;
import me.googas.chat.adapters.PlayerLocaleAdapter;
import org.bukkit.entity.Player;

/** This adapter uses the latest method to get the locale of a player. */
public final class LatestPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.getLocale();
  }
}
