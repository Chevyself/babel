package me.googas.chat.adapters.v1_11;

import lombok.NonNull;
import me.googas.chat.adapters.PlayerLocaleAdapter;
import org.bukkit.entity.Player;

public final class LegacyPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.spigot().getLocale();
  }
}
