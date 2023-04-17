package com.github.chevyself.babel.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

/** An adapter to send titles to players in different server versions. */
public interface PlayerTitleAdapter {

  /**
   * Send a title to a player.
   *
   * @param player the player to send the title
   * @param title the title to send
   * @param subtitle the subtitle to send
   * @param fadeIn the time to fade in
   * @param stay the time to stay
   * @param fadeOut the time to fade out
   */
  void sendTitle(
      @NonNull Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
}
