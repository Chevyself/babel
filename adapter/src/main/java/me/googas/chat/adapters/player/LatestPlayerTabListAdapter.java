package me.googas.chat.adapters.player;

import lombok.NonNull;
import me.googas.chat.adapters.PlayerTabListAdapter;
import org.bukkit.entity.Player;

/** This adapter uses the latest method to set the tab list of a player. */
public final class LatestPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, String header, String bottom) {
    player.setPlayerListHeaderFooter(header, bottom);
  }
}
