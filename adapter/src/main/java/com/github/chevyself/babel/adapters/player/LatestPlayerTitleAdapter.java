package com.github.chevyself.babel.adapters.player;

import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** This adapter uses the latest method to send a title to a player. */
public final class LatestPlayerTitleAdapter implements PlayerTitleAdapter {
  @Override
  public void sendTitle(
      @NonNull Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }
}
