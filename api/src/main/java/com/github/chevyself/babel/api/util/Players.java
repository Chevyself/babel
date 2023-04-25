package com.github.chevyself.babel.api.util;

import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.adapters.ObjectiveAdapter;
import com.github.chevyself.babel.adapters.PlayerLocaleAdapter;
import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import com.github.chevyself.babel.adapters.PlayerTabViewAdapter;
import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
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
import com.github.chevyself.babel.adapters.v1_19_2.LegacyTabViewAdapter;
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
    if (Versions.getBukkit().isAfter(11)) {
      return new LatestPlayerLocaleAdapter();
    } else {
      return new LegacyPlayerLocaleAdapter();
    }
  }

  /**
   * Get the adapter to send titles to players.
   *
   * <p>If the server version is greater than 1.11 it will use the Spigot built-in title API.
   * Otherwise, it will use the legacy method of sending titles in {@link LegacyPlayerTitleAdapter}
   *
   * @return the adapter
   */
  @NonNull
  public static PlayerTitleAdapter getTitleAdapter() {
    if (Versions.getBukkit().isAfter(11)) {
      return new LatestPlayerTitleAdapter();
    } else {
      return new LegacyPlayerTitleAdapter();
    }
  }

  /**
   * Get the adapter to set the player list tab list footer and header.
   *
   * <p>If the server version is greater than 1.13 it will use the Spigot built-in tab list API.
   * Otherwise, it will use the legacy method of setting the tab list in {@link
   * LegacyPlayerTabListAdapter}
   *
   * @return the adapter
   */
  @NonNull
  public static PlayerTabListAdapter getTabListAdapter() {
    if (Versions.getBukkit().isAfter(13)) {
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
    if (Versions.getBukkit().isBefore(9)) {
      return new LegacyBossBarAdapter();
    } else {
      return new LatestBossBarAdapter();
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
    if (Versions.getBukkit().isAfter(12, 0)) {
      return new LatestObjectiveAdapter();
    } else {
      return new LegacyObjectiveAdapter();
    }
  }

  /**
   * Get the adapter to change the tab view of players.
   *
   * <p>In 1.19.3 there were some packet changes related to the tab view, therefore the previous
   * method of changing the tab view was moved to {@link LegacyTabViewAdapter} and the new method
   * was created in {@link LatestTabViewAdapter}
   *
   * @return the adapter
   */
  @NonNull
  public static PlayerTabViewAdapter getTabViewAdapter() {
    if (Versions.getBukkit().isBefore(19, 3)) {
      return new LegacyTabViewAdapter();
    } else {
      return new LatestTabViewAdapter();
    }
  }
}
