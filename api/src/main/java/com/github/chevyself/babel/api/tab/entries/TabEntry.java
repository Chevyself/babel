package com.github.chevyself.babel.api.tab.entries;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.tab.TabSlot;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.entity.player.Skin;
import com.github.chevyself.babel.packet.entity.player.WrappedGameProfile;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfo;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;

/**
 * An entry that can be displayed inside a slot of a tab list. It is used to display the name, ping,
 * skin.
 */
public interface TabEntry extends Comparable<TabEntry> {

  /**
   * Converts the entry to a {@link WrappedPlayerInfo} that can be sent to the player. This tricks
   * the client to think that a player is in the tab list, which is used to display the name, ping,
   * skin.
   *
   * @param viewer the viewer of the tab list
   * @param packet the packet to modify
   * @param slot the slot to modify
   * @return the modified packet
   * @throws PacketHandlingException if the packet could not be modified
   */
  @NonNull
  default WrappedPlayerInfo playerInfoData(
      @NonNull OfflinePlayer viewer, @NonNull Packet packet, @NonNull TabSlot slot)
      throws PacketHandlingException {
    return WrappedPlayerInfo.construct(
        packet,
        this.getGameProfile(slot),
        this.getPing(slot),
        this.getGamemode(slot),
        WrappedChatComponent.of(this.getDisplay(slot).build(Channel.of(viewer))));
  }

  /**
   * Get the game profile of the entry. This is used to display the skin of the entry, it also sets
   * the name to be sorted.
   *
   * @param slot the slot to get the game profile
   * @return the game profile of the entry
   * @throws PacketHandlingException if the game profile could not be constructed
   */
  @NonNull
  default WrappedGameProfile getGameProfile(@NonNull TabSlot slot) throws PacketHandlingException {
    try {
      Skin skin = getSkin();
      WrappedGameProfile gameProfile =
          WrappedGameProfile.construct(slot.getUuid(), slot.asEntryName());
      if (skin != null) {
        gameProfile.getProperties().put("textures", skin.asProperty());
      }
      return gameProfile;
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not construct WrappedGameProfile", e);
    }
  }

  /**
   * Get the skin of the entry. This is used to display the skin of the entry.
   *
   * @return the skin of the entry or null if it does not have one
   */
  default Skin getSkin() {
    return null;
  }

  /**
   * Get the display of the entry. This is uses the name of the entry to display text.
   *
   * @param slot the slot to get the display
   * @return the display of the entry
   * @throws NullPointerException if the slot is null
   */
  @NonNull
  Text getDisplay(@NonNull TabSlot slot);

  /**
   * Get the game mode of the entry. This is used to display the game mode of the entry.
   *
   * @param slot the slot to get the game mode
   * @return the game mode of the entry
   * @throws NullPointerException if the slot is null
   */
  @NonNull
  WrappedEnumGameMode getGamemode(@NonNull TabSlot slot);

  /**
   * Get the ping of the entry. This is used to display the ping of the entry.
   *
   * @param slot the slot to get the ping
   * @return the ping of the entry
   * @throws NullPointerException if the slot is null
   */
  int getPing(@NonNull TabSlot slot);

  /**
   * Get the priority of the entry. This is used to sort the entries in the tab list.
   *
   * @return the priority of the entry
   */
  boolean canBeReplaced(@NonNull TabEntry entry);
}
