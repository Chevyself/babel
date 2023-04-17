package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.entity.Player;

/**
 * An adapter to create boss bars. This is used to create boss bars that are adapted to the server
 * version
 */
public interface BossBarAdapter {

  @Deprecated
  @NonNull
  AdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      WrappedBarColor color,
      WrappedBarStyle style);

  @Deprecated
  @NonNull
  AdaptedBossBar create(@NonNull Player player, @NonNull String title, float progress);

  /**
   * Create a boss bar for the player.
   *
   * @param player the player to create the boss bar for
   * @return the boss bar
   */
  @NonNull
  AdaptedBossBar create(@NonNull Player player);

    /**
     * Get the boss bar of the player.
     *
     * @param owner the owner of the boss bar
     * @return the boss bar if it exists
     */
  @NonNull
  Optional<? extends AdaptedBossBar> getBossBar(@NonNull UUID owner);
}
