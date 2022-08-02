package me.googas.chat.adapters;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

public class EmptyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;

  public EmptyAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
  }

  @Override
  public void setTitle(@NonNull String title) {}

  @Override
  public void setProgress(float progress) {}

  @Override
  public boolean isDestroyed() {
    return true;
  }

  @Override
  public void destroy() {}
}
