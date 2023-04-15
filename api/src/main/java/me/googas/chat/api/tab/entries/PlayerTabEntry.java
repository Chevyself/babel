package me.googas.chat.api.tab.entries;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.tab.TabSlot;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.entity.player.Skin;
import me.googas.chat.packet.entity.player.WrappedCraftPlayer;
import me.googas.chat.packet.world.WrappedEnumGameMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** A tab entry that represents a player. This can only be replaced by other player entries */
public class PlayerTabEntry implements TabEntry {

  @NonNull private final UUID uuid;

  /**
   * Create the entry
   *
   * @param uuid the uuid of the player
   */
  public PlayerTabEntry(@NonNull UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Create the entry
   *
   * @param player the player to represent
   */
  public PlayerTabEntry(@NonNull Player player) {
    this(player.getUniqueId());
  }

  /**
   * Get the player that is represented by this entry
   *
   * @return the player wrapped in an optional
   */
  @NonNull
  private Optional<Player> getPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.uuid));
  }

  @Override
  public @NonNull Line getDisplay(@NonNull TabSlot slot) {
    return Line.of(getPlayer().map(Player::getPlayerListName).orElse(""));
  }

  @Override
  public @NonNull WrappedEnumGameMode getGamemode(@NonNull TabSlot slot) {
    return getPlayer()
        .map(player -> WrappedEnumGameMode.valueOf(player.getGameMode()))
        .orElse(WrappedEnumGameMode.SURVIVAL);
  }

  @Override
  public int getPing(@NonNull TabSlot slot) {
    return 0;
  }

  @Override
  public boolean canBeReplaced(@NonNull TabEntry entry) {
    return entry instanceof PlayerTabEntry;
  }

  @Override
  public Skin getSkin() {
    return this.getPlayer()
        .map(
            player -> {
              try {
                return WrappedCraftPlayer.of(player).getHandle().getSkin();
              } catch (InvocationTargetException
                  | IllegalAccessException
                  | PacketHandlingException e) {
                ErrorHandler.getInstance()
                    .handle(Level.SEVERE, "Could not get skin from player", e);
                return null;
              }
            })
        .orElse(null);
  }

  @Override
  public int compareTo(@NonNull TabEntry o) {
    return !(o instanceof PlayerTabEntry)
        ? 0
        : this.getName().compareTo(((PlayerTabEntry) o).getName());
  }

  @NonNull
  private String getName() {
    return this.getPlayer().map(Player::getPlayerListName).orElse("");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlayerTabEntry that = (PlayerTabEntry) o;
    return uuid.equals(that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
}
