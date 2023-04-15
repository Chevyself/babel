package me.googas.chat.api.dependencies.papi;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

/**
 * Represents the soft dependency of PlaceholderAPI. This class will check if PlaceholderAPI is
 * present.
 */
public final class PapiSoft {

  @Getter private static boolean enabled;
  private static PapiPlaceholderBuilder builder;

  static {
    reload();
  }

  /**
   * Get the placeholder builder.
   *
   * @return the builder
   * @throws NullPointerException if the soft dependency is not enabled
   */
  @NonNull
  public static PapiPlaceholderBuilder getBuilder() {
    return Objects.requireNonNull(
        builder, "Builder has not been enabled. Check if it has been with #isEnabled");
  }

  /** Checks if the soft dependency is enabled. */
  public static void reload() {
    enabled = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    if (enabled) {
      builder = new PapiPlaceholderBuilder();
    }
  }
}
