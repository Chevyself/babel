package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * An adapted boss bar is a boss bar that is adapted to the server version. This is used to send
 * boss bars to players
 */
public interface AdaptedBossBar {

  /**
   * Get the unique id of the player that owns this boss bar. This is the player that will see the
   * boss bar
   *
   * @return the unique id of the player
   */
  @NonNull
  UUID getOwner();

  /**
   * Get the owner of this boss bar's entity.
   *
   * @return the owner of this boss bar's entity
   */
  @NonNull
  default Optional<Player> getOwnerBukkit() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getOwner()));
  }

  /**
   * Set the title of the boss bar.
   *
   * @param title the title of the boss bar
   * @return this same instance
   * @throws NullPointerException if the title is null
   */
  @NonNull
  AdaptedBossBar setTitle(@NonNull String title);

  /**
   * Set the progress of the boss bar. This is a float between 0 and 1, where 0 is empty and 1 is
   * full
   *
   * @param progress the progress of the boss bar
   * @return this same instance
   * @throws NullPointerException if the progress is null
   */
  @NonNull
  AdaptedBossBar setProgress(float progress);

  /**
   * Set the color of the boss bar
   *
   * @since 1.9
   * @param color the color of the boss bar
   * @return this same instance
   * @throws NullPointerException if the color is null
   */
  @NonNull
  AdaptedBossBar setColor(@NonNull WrappedBarColor color);

  /**
   * Set the style of the boss bar
   *
   * @since 1.9
   * @param style the style of the boss bar
   * @return this same instance
   * @throws NullPointerException if the style is null
   */
  @NonNull
  AdaptedBossBar setStyle(@NonNull WrappedBarStyle style);

  /**
   * Checks if the boss bar is destroyed. This means that the player has logged out and the boss bar
   * is no longer visible
   *
   * @return true if the boss bar is destroyed
   */
  boolean isDestroyed();

  /**
   * Checks if the boss bar is visible to the player
   *
   * @return true if the boss bar is visible
   */
  boolean isDisplayed();

  /**
   * Destroy the boss bar. This means that the boss bar will be removed from the player and will no
   * longer be visible
   */
  void destroy();

  /**
   * Display the boss bar to the player. This will make the boss bar visible to the player
   *
   * @return this same instance
   */
  @NonNull
  AdaptedBossBar display();
}
