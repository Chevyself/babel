package me.googas.chat.adapters.bossbar;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import org.bukkit.boss.BossBar;

public class LatestAdaptedBossBar implements AdaptedBossBar {

  @NonNull private final BossBar bukkit;
  @NonNull @Getter private final UUID owner;
  @Getter private boolean destroyed;

  LatestAdaptedBossBar(@NonNull BossBar bukkit, @NonNull UUID owner) {
    this.bukkit = bukkit;
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LatestAdaptedBossBar that = (LatestAdaptedBossBar) o;
    return owner.equals(that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner);
  }

  @Override
  public void setTitle(@NonNull String title) {
    if (this.getOwnerBukkit().isPresent()) {
      this.bukkit.setTitle(title);
    } else {
      this.destroy();
    }
  }

  @Override
  public void setProgress(float progress) {
    if (this.getOwnerBukkit().isPresent()) {
      this.setProgress(progress);
    } else {
      this.destroy();
    }
  }

  @Override
  public void destroy() {
    if (!this.destroyed) {
      this.destroyed = true;
      this.bukkit.removeAll();
    }
  }
}
