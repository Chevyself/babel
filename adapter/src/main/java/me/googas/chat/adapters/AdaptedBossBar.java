package me.googas.chat.adapters;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface AdaptedBossBar {

  @NonNull
  UUID getOwner();

  @NonNull
  default Optional<Player> getOwnerBukkit() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getOwner()));
  }

  void setTitle(@NonNull String title);

  void setProgress(float progress);

  boolean isDestroyed();

  void destroy();
}
