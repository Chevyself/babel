package com.github.chevyself.babel.adapters.tab;

import com.github.chevyself.babel.adapters.PlayerTabViewAdapter;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfoAction;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfoEntry;
import com.github.chevyself.reflect.debug.Debugger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.entity.Player;

/** Adapter for the tab view in versions '1.19.3' and up. */
public class LatestTabViewAdapter implements PlayerTabViewAdapter {

  @Override
  public void clear(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo) {
    try {
      this.sendRemove(viewer, playerInfo);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not clear tab view", e);
    }
  }

  @Override
  public void initialize(
      @NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo) {
    try {
      this.sendAdd(viewer, playerInfo);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not initialize tab view", e);
    }
  }

  @Override
  public void update(
      @NonNull Player player, boolean skin, @NonNull List<PlayerInfoAdapter> playerInfo) {
    try {
      if (skin) {
        this.sendRemove(player, playerInfo);
        this.sendAdd(player, playerInfo);
      } else {
        this.createPacket(WrappedPlayerInfoAction.UPDATE_DISPLAY_NAME)
            .setField(1, this.getEntries(playerInfo))
            .send(player);
      }
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not update tab view", e);
    }
  }

  private void sendAdd(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> info)
      throws PacketHandlingException {
    this.createPacket(
            WrappedPlayerInfoAction.ADD_PLAYER,
            WrappedPlayerInfoAction.UPDATE_DISPLAY_NAME,
            WrappedPlayerInfoAction.UPDATE_LATENCY,
            WrappedPlayerInfoAction.UPDATE_LISTED)
        .setField(1, this.getEntries(info))
        .send(viewer);
  }

  private void sendRemove(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> info)
      throws PacketHandlingException {
    PacketType.Play.ClientBound.PLAYER_INFO_REMOVE.create(this.getIds(info)).send(viewer);
  }

  @NonNull
  private Packet createPacket(@NonNull WrappedPlayerInfoAction... actions)
      throws PacketHandlingException {
    return PacketType.Play.ClientBound.PLAYER_INFO_UPDATE.create(
        WrappedPlayerInfoAction.toEnumSet(actions), new ArrayList<>());
  }

  private List<UUID> getIds(@NonNull Collection<PlayerInfoAdapter> info) {
    return info.stream().map(PlayerInfoAdapter::getUniqueId).collect(Collectors.toList());
  }

  @NonNull
  private List<?> getEntries(@NonNull Collection<PlayerInfoAdapter> info) {
    return info.stream()
        .map(PlayerInfoAdapter::asPlayerInfoEntry)
        .map(WrappedPlayerInfoEntry::getWrapped)
        .collect(Collectors.toList());
  }
}
