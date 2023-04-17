package me.googas.chat.adapters.bossbar;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;

public class EmptyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;

  public EmptyAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
  }

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
