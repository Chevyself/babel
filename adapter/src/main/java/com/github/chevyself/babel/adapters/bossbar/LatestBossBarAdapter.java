package com.github.chevyself.babel.adapters.bossbar;

import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** This is the latest implementation of {@link BossBarAdapter} that uses the latest Spigot API. */
public class LatestBossBarAdapter implements BossBarAdapter {

  @NonNull private final Set<LatestAdaptedBossBar> bossBars = new HashSet<>();

  @Override
  public @NonNull LatestAdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      WrappedBarColor color,
      WrappedBarStyle style) {
    LatestAdaptedBossBar bossBar = create(player).setTitle(title).setProgress(progress);
    if (color != null) {
      bossBar.setColor(color);
    }
    if (style != null) {
      bossBar.setStyle(style);
    }
    bossBars.add(bossBar);
    return bossBar.display();
  }

  @Override
  public @NonNull LatestAdaptedBossBar create(
      @NonNull Player player, @NonNull String title, float progress) {
    return this.create(player, title, progress, null, null);
  }

  @Override
  public @NonNull LatestAdaptedBossBar create(@NonNull Player player) {
    LatestAdaptedBossBar bossBar = new LatestAdaptedBossBar(player.getUniqueId());
    bossBars.add(bossBar);
    return bossBar;
  }

  @Override
  public @NonNull Optional<LatestAdaptedBossBar> getBossBar(@NonNull UUID owner) {
    return bossBars.stream()
        .filter(bossBar -> bossBar.getOwner().equals(owner) && !bossBar.isDestroyed())
        .findFirst();
  }
}
