package me.googas.chat.api.softdependencies.papi;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

public class PapiSoft {

  @Getter private static final boolean enabled;
  private static PapiPlaceholderBuilder builder;

  static {
    enabled = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    if (enabled) {
      builder = new PapiPlaceholderBuilder();
    }
  }

  @NonNull
  public static PapiPlaceholderBuilder getBuilder() {
    return Objects.requireNonNull(
        builder, "Builder has not been enabled. Check if it has been with #isEnabled");
  }
}
