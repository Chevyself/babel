package me.googas.chat.adapters.bossbar;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/** This is the latest implementation of {@link AdaptedBossBar} that uses the latest Spigot API. */
public class LatestAdaptedBossBar implements AdaptedBossBar {
  @NonNull @Getter private final UUID owner;
  @NonNull private String title;
  private float progress;
  @NonNull private WrappedBarColor color;
  @NonNull private WrappedBarStyle style;

  private BossBar bukkit;
  @Getter private boolean destroyed;

  public LatestAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
    this.title = "";
    this.progress = 1;
    this.color = WrappedBarColor.BLUE;
    this.style = WrappedBarStyle.SOLID;
    this.destroyed = false;
  }

  @NonNull
  @Override
  public LatestAdaptedBossBar display() {
    Optional<Player> owner = this.getOwnerBukkit();
    if (owner.isPresent()) {
      if (!this.isDisplayed()) {
        this.bukkit =
            Bukkit.createBossBar(this.title, this.color.getWrapped(), this.style.getWrapped());
        this.bukkit.setProgress(this.progress);
        this.bukkit.addPlayer(owner.get());
      }
    } else {
      this.destroy();
    }
    return this;
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
  @NonNull
  public LatestAdaptedBossBar setTitle(@NonNull String title) {
    if (this.getOwnerBukkit().isPresent()) {
      this.title = title;
      if (this.isDisplayed()) {
        this.bukkit.setTitle(title);
      }
    } else {
      this.destroy();
    }
    return this;
  }

  @Override
  @NonNull
  public LatestAdaptedBossBar setProgress(float progress) {
    if (this.getOwnerBukkit().isPresent() && progress >= 0) {
      this.progress = progress;
      if (this.isDisplayed()) {
        this.bukkit.setProgress(progress);
      }
    } else {
      this.destroy();
    }
    return this;
  }

  @Override
  public @NonNull AdaptedBossBar setColor(@NonNull WrappedBarColor color) {
    if (this.getOwnerBukkit().isPresent()) {
      this.color = color;
      if (this.isDisplayed()) {
        this.bukkit.setColor(color.getWrapped());
      }
    } else {
      this.destroy();
    }
    return this;
  }

  @Override
  public @NonNull AdaptedBossBar setStyle(@NonNull WrappedBarStyle style) {
    if (this.getOwnerBukkit().isPresent()) {
      this.style = style;
      if (this.isDisplayed()) {
        this.bukkit.setStyle(style.getWrapped());
      }
    } else {
      this.destroy();
    }
    return this;
  }

  @Override
  public boolean isDisplayed() {
    return this.bukkit != null;
  }

  @Override
  public void destroy() {
    if (!this.destroyed) {
      this.destroyed = true;
      if (this.bukkit != null) {
        this.bukkit.removeAll();
      }
    }
  }
}
