package com.github.chevyself.babel.api.listeners;

import com.github.chevyself.babel.api.channels.ChannelUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/** Listener class to keep consistency and free memory when handling channels. */
public class PlayerChannelListener implements Listener {

  /**
   * Removes the player's channel from the list of channels when they quit.
   *
   * @param event the PlayerQuitEvent
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    ChannelUtils.removePlayer(player.getUniqueId());
  }
}
