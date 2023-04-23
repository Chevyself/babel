package com.github.chevyself.babel.api.bossbar;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.adapters.v1_8.LegacyAdaptedBossBar;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.channels.PlayerChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A task to teleport wither boss bars to players.
 *
 * <p>This task must be registered when the server version is 1.8.
 */
public class WitherTask implements Runnable {

  @Override
  public void run() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      PlayerChannel channel = Channel.of(player);
      if (channel.hasBossBar()) {
        AdaptedBossBar bossBar = channel.getBossBar();
        if (bossBar instanceof LegacyAdaptedBossBar) {
          ((LegacyAdaptedBossBar) bossBar).teleport();
        }
      }
    }
  }
}
