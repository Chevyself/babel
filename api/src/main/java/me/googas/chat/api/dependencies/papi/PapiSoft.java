package me.googas.chat.api.dependencies.papi;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

public final class PapiSoft {

  @Getter private static boolean enabled;
  private static PapiPlaceholderBuilder builder;

  static {
    reload();
  }

  @NonNull
  public static PapiPlaceholderBuilder getBuilder() {
    return Objects.requireNonNull(
        builder, "Builder has not been enabled. Check if it has been with #isEnabled");
  }

  public static void reload() {
    enabled = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    if (enabled) {
      builder = new PapiPlaceholderBuilder();
    }
  }
}
