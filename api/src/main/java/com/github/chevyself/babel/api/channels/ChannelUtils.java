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
import lombok.NonNull;

/**
 * Utility class for channels.
 */
final class ChannelUtils {

  @NonNull public static final PlayerTabListAdapter tabListAdapter = Players.getTabListAdapter();
  @NonNull public static final PlayerTitleAdapter titleAdapter = Players.getTitleAdapter();
  @NonNull public static final BossBarAdapter bossBarAdapter = Players.getBossBarAdapter();
  @NonNull public static final Set<PlayerScoreboard> scoreboards = new HashSet<>();
  @NonNull public static final Set<PlayerTabView> views = new HashSet<>();
  @NonNull static final List<PlayerChannel> players = new ArrayList<>();
}