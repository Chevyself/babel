package com.github.chevyself.babel.api.channels;

import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import com.github.chevyself.babel.api.scoreboard.PlayerScoreboard;
import com.github.chevyself.babel.api.tab.PlayerTabView;
import com.github.chevyself.babel.api.util.Players;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;

/** Utility class for channels. */
public final class ChannelUtils {

  @NonNull static final PlayerTabListAdapter tabListAdapter = Players.getTabListAdapter();
  @NonNull static final PlayerTitleAdapter titleAdapter = Players.getTitleAdapter();
  @NonNull static final BossBarAdapter bossBarAdapter = Players.getBossBarAdapter();
  @NonNull static final Set<PlayerScoreboard> scoreboards = new HashSet<>();
  @NonNull static final Set<PlayerTabView> views = new HashSet<>();
  @NonNull static final List<PlayerChannel> players = new ArrayList<>();

  /**
   * Removes a player from the list of cached players channels.
   *
   * @param uuid the uuid of the player to remove
   * @return true if the player was removed, false otherwise
   */
  public static boolean removePlayer(@NonNull UUID uuid) {
    return ChannelUtils.players.removeIf(channel -> channel.getUniqueId().equals(uuid));
  }
}
