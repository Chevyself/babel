package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public final class LatestPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.getLocale();
  }
}
