package me.googas.chat.adapters.bossbar;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.adapters.BossBarAdapter;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.entity.Player;

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
