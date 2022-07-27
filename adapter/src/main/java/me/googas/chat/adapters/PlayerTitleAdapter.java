package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public interface PlayerTitleAdapter {
  void sendTitle(
      @NonNull Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
}
