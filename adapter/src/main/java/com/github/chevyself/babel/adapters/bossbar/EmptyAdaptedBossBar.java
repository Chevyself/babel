package com.github.chevyself.babel.adapters.bossbar;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

/** An empty {@link AdaptedBossBar} that does nothing. This is to avoid null checks */
public class EmptyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;

  /**
   * Create a new empty boss bar with the given owner.
   *
   * @param owner the owner of the boss bar
   */
  public EmptyAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
  }

  /** Create a new empty boss bar with a random owner. */
  public EmptyAdaptedBossBar() {
    this(UUID.randomUUID());
  }

  @Override
  public @NonNull AdaptedBossBar setTitle(@NonNull String title) {
    return this;
  }

  @Override
  public @NonNull AdaptedBossBar setProgress(float progress) {
    return this;
  }

  @Override
  public @NonNull AdaptedBossBar setColor(@NonNull WrappedBarColor color) {
    return this;
  }

  @Override
  public @NonNull AdaptedBossBar setStyle(@NonNull WrappedBarStyle style) {
    return this;
  }

  @Override
  public boolean isDestroyed() {
    return true;
  }

  @Override
  public boolean isDisplayed() {
    return false;
  }

  @Override
  public void destroy() {}

  @Override
  public @NonNull AdaptedBossBar display() {
    return this;
  }
}
