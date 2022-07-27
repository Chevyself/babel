package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public interface PlayerLocaleAdapter {
  @NonNull
  String getLocale(@NonNull Player player);
}
