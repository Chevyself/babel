package me.googas.chat.api.softdependencies.papi;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class PapiPlaceholderBuilder {

  @NonNull
  public String build(@NonNull OfflinePlayer player, @NonNull String raw) {
    return PlaceholderAPI.setPlaceholders(player, raw);
  }
}
