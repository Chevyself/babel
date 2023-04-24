package com.github.chevyself.babel.adapters.v1_19_2;

import com.github.chevyself.babel.adapters.PlayerTabViewAdapter;
import com.github.chevyself.babel.adapters.tab.PlayerInfoAdapter;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.entity.player.WrappedEntityPlayer;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfo;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfoAction;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.modifiers.CollectionModifier;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Adapter for the tab view in versions equal or prior to 1.19.2.
 */
public class LegacyTabViewAdapter implements PlayerTabViewAdapter {

  private Packet createPacket(@NonNull WrappedPlayerInfoAction action)
      throws PacketHandlingException {
    return PacketType.Play.ClientBound.PLAYER_INFO.create(
        action.getWrapped(), WrappedEntityPlayer.createArray(0));
  }

  @Override
  public void clear(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo) {
    try {
      Packet packet = this.createPacket(WrappedPlayerInfoAction.REMOVE_PLAYER);
      packet.setField(1, CollectionModifier.addWrappers(this.wrap(playerInfo)));
      packet.send(viewer);
    } catch (PacketHandlingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(@NonNull Player viewer, @NonNull Collection<PlayerInfoAdapter> playerInfo) {
    try {
      Packet packet = this.createPacket(WrappedPlayerInfoAction.ADD_PLAYER);
      packet.setField(1, CollectionModifier.addWrappers(this.wrap(playerInfo)));
      packet.send(viewer);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not initialize tab view", e);
    }
  }

  @Override
  public void update(
      @NonNull Player player, boolean skin, @NonNull List<PlayerInfoAdapter> playerInfo) {
    try {
      Packet packet = this.createPacket(WrappedPlayerInfoAction.REMOVE_PLAYER);
      List<WrappedPlayerInfo> playerInfoList = this.wrap(playerInfo);
      packet.setField(1, CollectionModifier.addWrappers(playerInfoList));
      if (skin) {
        // If the skin is updated we need to remove the player first
        packet.send(player);
        // In versions previos to 1.17 we could just use the same packet to update the skin,
        // but now we need to send a new packet
        packet = this.createPacket(WrappedPlayerInfoAction.ADD_PLAYER);
        packet.setField(1, CollectionModifier.addWrappers(playerInfoList));
        packet.send(player);
      } else {
        packet.setField(0, WrappedPlayerInfoAction.UPDATE_DISPLAY_NAME);
        packet.send(player);
      }
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not update tab view", e);
    }
  }

  @NonNull
  private List<WrappedPlayerInfo> wrap(@NonNull Collection<PlayerInfoAdapter> info) {
    return info.stream().map(PlayerInfoAdapter::asPlayerInfoData).collect(Collectors.toList());
  }
}
