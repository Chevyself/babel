package me.googas.chat.api.tab;

import java.util.UUID;
import lombok.Data;
import lombok.NonNull;
import me.googas.chat.api.tab.entries.TabEntry;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.entity.player.WrappedPlayerInfo;
import org.bukkit.OfflinePlayer;

/**
 * Represents a slot of the tab list. This is used to store the {@link TabEntry}.
 *
 * <p>This class is mutable and is not thread safe
 */
@Data
public final class TabSlot implements Comparable<TabSlot> {

  @NonNull private final UUID uuid = UUID.randomUUID();
  @NonNull private final TabCoordinate coordinate;
  @NonNull private TabEntry entry;

  /**
   * Converts the slot into a {@link WrappedPlayerInfo} to be sent to the player inside a packet.
   *
   * @see TabEntry#playerInfoData(OfflinePlayer, Packet, TabSlot)
   * @param viewer the player to send the packet to
   * @param packet the packet to send
   * @return the {@link WrappedPlayerInfo} to be sent
   * @throws PacketHandlingException if the packet could not be handled
   * @throws NullPointerException if the viewer or packet is null
   */
  public WrappedPlayerInfo playerInfoData(@NonNull OfflinePlayer viewer, @NonNull Packet packet)
      throws PacketHandlingException {
    return entry.playerInfoData(viewer, packet, this);
  }

  /**
   * Get the name of the entry to be displayed in the tab list. Minecraft sorts players in the tab
   * by their name, so this will use numbers to trick the client into thinking that the player is in
   * the right position.
   *
   * @return the name of the entry
   */
  @NonNull
  public String asEntryName() {
    return " "
        + String.format("%02d", coordinate.getX())
        + ","
        + String.format("%02d", coordinate.getY());
  }

  /**
   * Set the entry of this slot.
   *
   * @param entry the entry to set
   * @throws NullPointerException if the entry is null
   */
  public void setEntry(@NonNull TabEntry entry) {
    this.entry = entry;
  }

  @Override
  public int compareTo(@NonNull TabSlot o) {
    return this.getCoordinate().compareTo(o.getCoordinate());
  }
}
