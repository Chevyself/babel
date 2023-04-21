package com.github.chevyself.babel.adapters.player;

import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/** This adapter uses the latest method to set the tab list of a player. */
public final class LatestPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, @Nullable String header, @Nullable String bottom) {
    player.setPlayerListHeaderFooter(header, bottom);
  }
}
