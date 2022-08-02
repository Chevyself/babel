package me.googas.chat.adapters;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.wrappers.WrappedBarColor;
import me.googas.chat.wrappers.WrappedBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class LatestBossBarAdapter implements BossBarAdapter {

  @NonNull private final Set<LatestAdaptedBossBar> bossBars = new HashSet<>();

  @Override
  public AdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      WrappedBarColor color,
      WrappedBarStyle style) {
    BarColor baseColor = BarColor.BLUE;
    BarStyle baseStyle = BarStyle.SOLID;
    BossBar bossBar =
        Bukkit.createBossBar(
            title,
            color == null ? baseColor : color.get().orElse(baseColor),
            style == null ? baseStyle : style.get().orElse(baseStyle));
    bossBar.setProgress(progress);
    bossBar.addPlayer(player);
    LatestAdaptedBossBar adaptedBossBar = new LatestAdaptedBossBar(bossBar, player.getUniqueId());
    bossBars.add(adaptedBossBar);
    return adaptedBossBar;
  }

  @Override
  public AdaptedBossBar create(@NonNull Player player, @NonNull String title, float progress) {
    return this.create(player, title, progress, WrappedBarColor.BLUE, WrappedBarStyle.SOLID);
  }

  @Override
  public @NonNull Optional<LatestAdaptedBossBar> getBossBar(@NonNull UUID owner) {
    return bossBars.stream()
        .filter(bossBar -> bossBar.getOwner().equals(owner) && !bossBar.isDestroyed())
        .findFirst();
  }
}
