package com.github.chevyself.babel.api.util;

import com.github.chevyself.babel.adapter.v1_19_2.LegacyTabViewAdapter;
import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.adapters.ObjectiveAdapter;
import com.github.chevyself.babel.adapters.PlayerLocaleAdapter;
import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import com.github.chevyself.babel.adapters.TabViewAdapter;
import com.github.chevyself.babel.adapters.bossbar.LatestBossBarAdapter;
import com.github.chevyself.babel.adapters.player.LatestPlayerLocaleAdapter;
import com.github.chevyself.babel.adapters.player.LatestPlayerTabListAdapter;
import com.github.chevyself.babel.adapters.player.LatestPlayerTitleAdapter;
import com.github.chevyself.babel.adapters.scoreboard.LatestObjectiveAdapter;
import com.github.chevyself.babel.adapters.tab.LatestTabViewAdapter;
import com.github.chevyself.babel.adapters.v1_11.LegacyPlayerLocaleAdapter;
import com.github.chevyself.babel.adapters.v1_11.LegacyPlayerTabListAdapter;
import com.github.chevyself.babel.adapters.v1_11.LegacyPlayerTitleAdapter;
import com.github.chevyself.babel.adapters.v1_12.LegacyObjectiveAdapter;
import com.github.chevyself.babel.adapters.v1_8.LegacyBossBarAdapter;
import com.github.chevyself.babel.util.Versions;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** Utility class to provide adapters of different Minecraft versions of players. */
public final class Players {

  @NonNull private static final PlayerLocaleAdapter localeAdapter = Players.getLocaleAdapter();

  /**
   * Get the locale language of a players game.
   *
   * @param player the player to get its locale language
   * @return the locale language of a player
   * @throws NullPointerException if the player is null
   */
  @NonNull
  public static String getLocale(@NonNull Player player) {
    return Players.localeAdapter.getLocale(player);
  }

  @NonNull
  private static PlayerLocaleAdapter getLocaleAdapter() {
    if (Versions.BUKKIT > 11) {
      return new LatestPlayerLocaleAdapter();
    } else {
      return new LegacyPlayerLocaleAdapter();
    }
  }

  @NonNull
  public static PlayerTitleAdapter getTitleAdapter() {
    if (Versions.BUKKIT > 11) {
      return new LatestPlayerTitleAdapter();
    } else {
      return new LegacyPlayerTitleAdapter();
    }
  }

  @NonNull
  public static PlayerTabListAdapter getTabListAdapter() {
    if (Versions.BUKKIT > 13) {
      return new LatestPlayerTabListAdapter();
    } else {
      return new LegacyPlayerTabListAdapter();
    }
  }

  /**
   * Get the boos-bar adapter. If the server version is greater than 1.8 it will use the Spigot
   * built-in boos-bar API.
   *
   * @return the adapter
   */
  @NonNull
  public static BossBarAdapter getBossBarAdapter() {
    if (Versions.BUKKIT > 8) {
      return new LatestBossBarAdapter();
    } else {
      return new LegacyBossBarAdapter();
    }
  }

  /**
   * Get the objective adapter. If the server version is greater than 1.12 it will use the latest
   * methods in Spigot API.
   *
   * @return the adapter
   */
  @NonNull
  public static ObjectiveAdapter getObjectiveAdapter() {
    if (Versions.BUKKIT > 12) {
      return new LatestObjectiveAdapter();
    } else {
      return new LegacyObjectiveAdapter();
    }
  }

  public static TabViewAdapter getTabViewAdapter() {
    if (Versions.getBukkit().isPrevious(19, 3)) {
      return new LegacyTabViewAdapter();
    } else {
      return new LatestTabViewAdapter();
    }
  }
}
