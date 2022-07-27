package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public final class LatestPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, String header, String bottom) {
    player.setPlayerListHeaderFooter(header, bottom);
  }
}
