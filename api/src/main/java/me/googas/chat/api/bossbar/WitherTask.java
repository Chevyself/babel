package me.googas.chat.api.bossbar;

import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.adapters.v1_8.LegacyAdaptedBossBar;
import me.googas.chat.api.channels.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WitherTask implements Runnable {

  @Override
  public void run() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      AdaptedBossBar bossBar = Channel.of(player).getBossBar();
      if (bossBar instanceof LegacyAdaptedBossBar) {
        ((LegacyAdaptedBossBar) bossBar).teleport();
      }
    }
  }
}
