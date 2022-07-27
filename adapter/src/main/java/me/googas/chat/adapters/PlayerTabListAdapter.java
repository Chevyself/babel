package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public interface PlayerTabListAdapter {
  void setTabList(@NonNull Player player, String header, String bottom);
}
