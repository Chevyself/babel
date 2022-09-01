package me.googas.chat.api.tab;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import lombok.Data;
import lombok.NonNull;
import me.googas.chat.api.tab.entries.TabEntry;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.entity.player.WrappedPlayerInfo;
import org.bukkit.OfflinePlayer;

@Data
public class TabSlot implements Comparable<TabSlot> {

  @NonNull private final UUID uuid = UUID.randomUUID();
  @NonNull private final TabCoordinate coordinate;
  @NonNull private TabEntry entry;

  public WrappedPlayerInfo playerInfoData(@NonNull OfflinePlayer viewer, @NonNull Packet packet)
      throws PacketHandlingException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    return entry.playerInfoData(viewer, packet, this);
  }

  @NonNull
  public String asEntryName() {
    return " "
        + String.format("%02d", coordinate.getX())
        + ","
        + String.format("%02d", coordinate.getY());
  }

  public void setEntry(@NonNull TabEntry entry) {
    this.entry = entry;
  }

  @Override
  public int compareTo(@NonNull TabSlot o) {
    return this.getCoordinate().compareTo(o.getCoordinate());
  }
}
