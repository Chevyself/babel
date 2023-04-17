package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.entity.Player;

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

  @NonNull
  AdaptedBossBar create(@NonNull Player player);

  @NonNull
  Optional<? extends AdaptedBossBar> getBossBar(@NonNull UUID owner);
}
