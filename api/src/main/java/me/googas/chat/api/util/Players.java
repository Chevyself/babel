package me.googas.chat.api.util;

import lombok.NonNull;
import me.googas.chat.adapters.BossBarAdapter;
import me.googas.chat.adapters.LatestBossBarAdapter;
import me.googas.chat.adapters.LatestPlayerLocaleAdapter;
import me.googas.chat.adapters.LatestPlayerTabListAdapter;
import me.googas.chat.adapters.LatestPlayerTitleAdapter;
import me.googas.chat.adapters.PlayerLocaleAdapter;
import me.googas.chat.adapters.PlayerTabListAdapter;
import me.googas.chat.adapters.PlayerTitleAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerLocaleAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerTabListAdapter;
import me.googas.chat.adapters.v1_11.LegacyPlayerTitleAdapter;
import me.googas.chat.adapters.v1_8.LegacyBossBarAdapter;
import org.bukkit.entity.Player;

public final class Players {

  @NonNull private static final PlayerLocaleAdapter localeAdapter = Players.getLocaleAdapter();

  /**
   * Get the locale language of a players game.
   *
   * @param player the player to get its locale language
   * @return the locale language of a player
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

  @NonNull
  public static BossBarAdapter getBossBarAdapter() {
    if (Versions.BUKKIT > 8) {
      return new LatestBossBarAdapter();
    } else {
      return new LegacyBossBarAdapter();
    }
  }
}
