package me.googas.chat.api.dependencies.viaversion;

import com.viaversion.viaversion.api.Via;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.api.channels.Channel;
import me.googas.chat.api.channels.PlayerChannel;
import me.googas.chat.api.util.Versions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * Represents the protocol of ViaVersion. This class will be used to get the version of a player and
 * store it in a map.
 */
public final class ViaVersionProtocol implements Listener {

  @NonNull private final Map<UUID, Versions.Player> versions = new HashMap<>();
  @NonNull private final Set<ProtocolPlayerChannel> channels = new HashSet<>();

  /**
   * Get the channel for a {@link Player} with its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the created channel for the player
   */
  @NonNull
  public ProtocolPlayerChannel getChannel(@NonNull UUID uniqueId) {
    return channels.stream()
        .filter(channel -> channel.getUniqueId().equals(uniqueId))
        .findFirst()
        .orElseGet(
            () -> {
              ProtocolPlayerChannel channel = new ProtocolPlayerChannel(uniqueId);
              channels.add(channel);
              return channel;
            });
  }

  /**
   * Get the version of the player.
   *
   * @param player the player to get the version
   * @return the version of the player
   */
  @NonNull
  public Versions.Player getVersion(@NonNull Player player) {
    return versions.computeIfAbsent(
        player.getUniqueId(),
        uuid -> Versions.getVersion(Via.getAPI().getPlayerVersion(player.getUniqueId())));
  }

  public void register(@NonNull Plugin plugin) {
    Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
  }

  /**
   * Remove the player version from the map when the player quits the game.
   *
   * @param event the event of the player quiting
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event) {
    versions.remove(event.getPlayer().getUniqueId());
  }

  /**
   * Add the version of the player to the map and set the version on its {@link
   * ProtocolPlayerChannel}.
   *
   * @param event the event of a player joining the game
   */
  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PlayerChannel playerChannel = Channel.of(player);
    if (playerChannel instanceof ProtocolPlayerChannel) {
      ((ProtocolPlayerChannel) playerChannel).setVersion(this.getVersion(player));
    }
  }
}
