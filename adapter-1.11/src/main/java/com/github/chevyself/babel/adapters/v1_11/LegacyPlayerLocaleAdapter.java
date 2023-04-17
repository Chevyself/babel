package com.github.chevyself.babel.adapters.v1_11;

import com.github.chevyself.babel.adapters.PlayerLocaleAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;

public final class LegacyPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.spigot().getLocale();
  }
}
