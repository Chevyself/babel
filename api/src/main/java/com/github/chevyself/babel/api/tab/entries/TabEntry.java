package com.github.chevyself.babel.api.tab.entries;

import com.github.chevyself.babel.adapters.tab.PlayerInfoAdapter;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.tab.TabSlot;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.authlib.WrappedGameProfile;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.entity.player.Skin;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfo;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
  @Deprecated
  @NonNull
  default WrappedPlayerInfo playerInfoData(
      @NonNull OfflinePlayer viewer, @Nullable Packet packet, @NonNull TabSlot slot)
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
    Skin skin = this.getSkin();
    WrappedGameProfile gameProfile =
        WrappedGameProfile.construct(slot.getUuid(), slot.asEntryName());
    if (skin != null) {
      gameProfile.getProperties().put("textures", skin.asProperty());
    }
    return gameProfile;
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
   * Get the priority of the entry. This is used to sort the entries in the tab list.
   *
   * @return the priority of the entry
   */
  default int getPriority() {
    return 0;
  }

  @Override
  default int compareTo(@NonNull TabEntry o) {
    // The higher the priority, the higher the entry is in the tab list
    return Integer.compare(o.getPriority(), this.getPriority());
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
   * Get whether this entry can be replaced by another entry.
   *
   * <p>For instance {@link EmptyTabEntry} can be replaced, meanwhile {@link PlayerTabEntry} can
   * only be replaced by another {@link PlayerTabEntry}.
   *
   * @param entry the entry to compare
   * @return whether this entry can be replaced by another entry
   */
  boolean canBeReplaced(@NonNull TabEntry entry);

  /**
   * Get the display of the entry. This is used to be set in packets
   *
   * @param viewer the viewer of the tab list
   * @param slot the slot that holds the entry
   * @return the display of the entry
   */
  @NonNull
  default WrappedChatComponent getDisplay(@NonNull Player viewer, @NonNull TabSlot slot) {
    return WrappedChatComponent.of(this.getDisplay(slot).build(Channel.of(viewer)));
  }

  /**
   * Get the adapter of the entry. This is used to be sent in packets
   *
   * @param viewer the viewer of the tab list
   * @param slot the slot that holds the entry
   * @return the adapter of the entry
   * @throws PacketHandlingException if the adapter could not be constructed
   */
  @NonNull
  default PlayerInfoAdapter toAdapter(@NonNull Player viewer, @NonNull TabSlot slot)
      throws PacketHandlingException {
    return new PlayerInfoAdapter(
        this.getGameProfile(slot),
        this.getPing(slot),
        this.getGamemode(slot),
        this.getDisplay(viewer, slot));
  }
}
