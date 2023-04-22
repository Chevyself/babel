package com.github.chevyself.babel.adapters.v1_11;

import com.github.chevyself.babel.adapters.PlayerLocaleAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** An adapter to get the player's locale in Minecraft 1.11 or lower. */
public final class LegacyPlayerLocaleAdapter implements PlayerLocaleAdapter {

  @Override
  public @NonNull String getLocale(@NonNull Player player) {
    return player.spigot().getLocale();
  }
}
