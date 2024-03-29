package com.github.chevyself.babel.adapters.v1_8;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/** Factory for creating boss bars for 1.8 clients. */
public class LegacyBossBarAdapter implements BossBarAdapter {

  @NonNull private final Set<LegacyAdaptedBossBar> bossBars = new HashSet<>();

  @Override
  public @NonNull AdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      @Nullable WrappedBarColor color,
      @Nullable WrappedBarStyle style) {
    return this.create(player, title, progress);
  }

  @Override
  public @NonNull AdaptedBossBar create(
      @NonNull Player player, @NonNull String title, float progress) {
    return this.create(player).setTitle(title).setProgress(progress).display();
  }

  @Override
  public @NonNull AdaptedBossBar create(@NonNull Player player) {
    LegacyAdaptedBossBar bossBar = new LegacyAdaptedBossBar(player.getUniqueId());
    this.bossBars.add(bossBar);
    return bossBar;
  }

  @Override
  public @NonNull Optional<LegacyAdaptedBossBar> getBossBar(@NonNull UUID owner) {
    return bossBars.stream()
        .filter(bossBar -> bossBar.getOwner().equals(owner) && !bossBar.isDestroyed())
        .findFirst();
  }
}
