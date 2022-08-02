package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.wrappers.WrappedBarColor;
import me.googas.chat.wrappers.WrappedBarStyle;
import org.bukkit.entity.Player;

public interface BossBarAdapter {

  AdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      WrappedBarColor color,
      WrappedBarStyle style);

  AdaptedBossBar create(@NonNull Player player, @NonNull String title, float progress);

  @NonNull
  Optional<? extends AdaptedBossBar> getBossBar(@NonNull UUID owner);
}
