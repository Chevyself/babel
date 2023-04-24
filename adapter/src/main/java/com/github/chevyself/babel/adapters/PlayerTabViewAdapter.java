package com.github.chevyself.babel.adapters;

import com.github.chevyself.babel.adapters.tab.PlayerInfoAdapter;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Adapter for the tab view of the client.
 */
public interface PlayerTabViewAdapter {

  /**
   * Remove all the fake players from the tab view.
   *
   * @param viewer the player owner of the tab view
   * @param playerInfo the fake players to remove
   */
  void clear(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo);

  /**
   * Adds all the fake players to the tab view.
   *
   * @param viewer the player owner of the tab view
   * @param playerInfo the fake players to add
   */
  void initialize(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo);

  /**
   * Update a list of fake players in the tab view.
   *
   * @param player the player owner of the tab view
   * @param skin whether the skin of the fake players should be updated
   * @param playerInfo the fake players to update
   */
  void update(@NonNull Player player, boolean skin, @NonNull List<PlayerInfoAdapter> playerInfo);
}
