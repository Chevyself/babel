package com.github.chevyself.babel.adapters.bossbar;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents many {@link AdaptedBossBar} as one. This is useful when you want to display the same
 * boss bar to many players
 */
public class AdaptedBossBarTuple implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner = UUID.randomUUID();
  @NonNull private final Set<AdaptedBossBar> bossBars;

  /**
   * Create a new boss bar tuple with the given boss bars.
   *
   * @param bossBars the boss bars to use
   */
  public AdaptedBossBarTuple(@NonNull Set<AdaptedBossBar> bossBars) {
    this.bossBars = bossBars;
  }

  /**
   * Create a new boss bar tuple with the given boss bars.
   *
   * @param bossBars the boss bars to use
   */
  public AdaptedBossBarTuple(@NonNull Collection<AdaptedBossBar> bossBars) {
    this(new HashSet<>(bossBars));
  }

  @Override
  public @NonNull AdaptedBossBarTuple setTitle(@NonNull String title) {
    this.bossBars.forEach(bossBar -> bossBar.setTitle(title));
    return this;
  }

  @Override
  public @NonNull AdaptedBossBarTuple setProgress(float progress) {
    this.bossBars.forEach(bossBar -> bossBar.setProgress(progress));
    return this;
  }

  @Override
  public @NonNull AdaptedBossBarTuple setColor(@NonNull WrappedBarColor color) {
    this.bossBars.forEach(bossBar -> bossBar.setColor(color));
    return this;
  }

  @Override
  public @NonNull AdaptedBossBarTuple setStyle(@NonNull WrappedBarStyle style) {
    this.bossBars.forEach(bossBar -> bossBar.setStyle(style));
    return this;
  }

  @Override
  public boolean isDestroyed() {
    return this.bossBars.stream().anyMatch(AdaptedBossBar::isDestroyed);
  }

  @Override
  public boolean isDisplayed() {
    return this.bossBars.stream().allMatch(AdaptedBossBar::isDisplayed);
  }

  @Override
  public void destroy() {
    this.bossBars.forEach(AdaptedBossBar::destroy);
  }

  @Override
  public @NonNull AdaptedBossBarTuple display() {
    this.bossBars.forEach(AdaptedBossBar::display);
    return this;
  }
}
