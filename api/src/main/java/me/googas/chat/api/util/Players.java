package me.googas.chat.api.util;

import lombok.NonNull;
import me.googas.chat.adapters.*;
import me.googas.chat.adapters.bossbar.LatestBossBarAdapter;
import me.googas.chat.adapters.player.LatestPlayerLocaleAdapter;
import me.googas.chat.adapters.player.LatestPlayerTabListAdapter;
import me.googas.chat.adapters.player.LatestPlayerTitleAdapter;
import me.googas.chat.adapters.scoreboard.LatestObjectiveAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerLocaleAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerTabListAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerTitleAdapter;
import me.googas.chat.adapters.v1_12.LegacyObjectiveAdapter;
import me.googas.chat.adapters.v1_8.LegacyBossBarAdapter;
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
}
