package me.googas.chat.api.dependencies.viaversion;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.util.Versions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Represents the soft dependency of ViaVersion. This class will check if ViaVersion is present.
 */
public final class ViaVersionSoft {

  @Getter private static boolean enabled;
  private static ViaVersionProtocol protocol;

  static {
    reload();
  }

  /**
   * Checks if the soft dependency is enabled.
   */
  private static void reload() {
    enabled = Bukkit.getServer().getPluginManager().getPlugin("ViaVersion") != null;
    if (enabled) {
      protocol = new ViaVersionProtocol();
    } else {
      protocol = null;
    }
  }

  /**
   * Get the protocol of ViaVersion.
   *
   * @return the protocol
   * @throws NullPointerException if the protocol is not enabled
   */
  @NonNull
  public static ViaVersionProtocol getProtocol() {
    return Objects.requireNonNull(
        protocol, "Protocol has not been initialized. Check if it has been using #isEnabled");
  }

    /**
     * Register the protocol to the plugin.
     *
     * @param plugin the plugin to register the protocol
     * @throws NullPointerException if the protocol is not enabled
     */
  public static void registerProtocol(@NonNull Plugin plugin) {
    Bukkit.getServer().getPluginManager().registerEvents(getProtocol(), plugin);
  }

    /**
     * Get the client version of the player.
     *
     * @param player the player to get the version
     * @return the version of the player
     * @throws NullPointerException if the player is null
     */
  @NonNull
  public static Versions.Player getVersion(@NonNull Player player) {
    return getProtocol().getVersion(player);
  }

    /**
     * Get the channel of the player. A protocol channel is used for legacy clients.
     *
     * @param uniqueId the unique id of the player
     * @return the channel of the player
     */
  @NonNull
  public static PlayerChannel getProtocolChannel(@NonNull UUID uniqueId) {
    return getProtocol().getChannel(uniqueId);
  }
}
