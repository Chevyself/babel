package me.googas.chat.api.softdependencies.viaversion;

import com.viaversion.viaversion.api.Via;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.util.Versions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ViaVersionProtocol implements Listener {

  @NonNull private final Map<UUID, Versions.Player> versions = new HashMap<>();

  /**
   * Get the channel for a {@link Player} with its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the created channel for the player
   */
  @NonNull
  public PlayerChannel getChannel(@NonNull UUID uniqueId) {
    return new ProtocolPlayerChannel(uniqueId);
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
