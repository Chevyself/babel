package com.github.chevyself.babel.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/** An adapter to set the tab list of a player in different server versions. */
public interface PlayerTabListAdapter {

  /**
   * Set the tab list of a player.
   *
   * @param player the player to set the tab list
   * @param header the header of the tab list
   * @param bottom the bottom of the tab list
   */
  void setTabList(@NonNull Player player, @Nullable String header, @Nullable String bottom);
}
