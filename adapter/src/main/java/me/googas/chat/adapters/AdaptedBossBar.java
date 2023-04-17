package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface AdaptedBossBar {

  @NonNull
  UUID getOwner();

  @NonNull
  default Optional<Player> getOwnerBukkit() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getOwner()));
  }

  @NonNull
  AdaptedBossBar setTitle(@NonNull String title);

  @NonNull
  AdaptedBossBar setProgress(float progress);

  @NonNull
  AdaptedBossBar setColor(@NonNull WrappedBarColor color);

  @NonNull
  AdaptedBossBar setStyle(@NonNull WrappedBarStyle style);

  boolean isDestroyed();

  boolean isDisplayed();

  void destroy();

  @NonNull
  AdaptedBossBar display();
}
