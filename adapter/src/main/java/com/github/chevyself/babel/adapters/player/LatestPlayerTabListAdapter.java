package com.github.chevyself.babel.adapters.player;

import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** This adapter uses the latest method to set the tab list of a player. */
public final class LatestPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, String header, String bottom) {
    player.setPlayerListHeaderFooter(header, bottom);
  }
}
