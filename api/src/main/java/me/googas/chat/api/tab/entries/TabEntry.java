package me.googas.chat.api.tab.entries;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.tab.TabSlot;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.chat.WrappedChatComponent;
import me.googas.chat.packet.entity.player.Skin;
import me.googas.chat.packet.entity.player.WrappedGameProfile;
import me.googas.chat.packet.entity.player.WrappedPlayerInfo;
import me.googas.chat.packet.world.WrappedEnumGameMode;
import org.bukkit.OfflinePlayer;

public interface TabEntry extends Comparable<TabEntry> {
  @NonNull
  default WrappedPlayerInfo playerInfoData(
      @NonNull OfflinePlayer viewer, @NonNull Packet packet, @NonNull TabSlot slot)
      throws InvocationTargetException, InstantiationException, IllegalAccessException,
          PacketHandlingException {
    return WrappedPlayerInfo.construct(
        packet,
        this.getGameProfile(slot),
        this.getPing(slot),
        this.getGamemode(slot),
        WrappedChatComponent.of(this.getDisplay(slot).build(Channel.of(viewer))));
  }

  @NonNull
  default WrappedGameProfile getGameProfile(@NonNull TabSlot slot)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    Skin skin = getSkin();
    WrappedGameProfile gameProfile =
        WrappedGameProfile.construct(slot.getUuid(), slot.asEntryName());
    if (skin != null) {
      gameProfile.getProperties().put("textures", skin.asProperty());
    }
    return gameProfile;
  }

  default Skin getSkin() {
    return null;
  }

  @NonNull
  Line getDisplay(@NonNull TabSlot slot);

  @NonNull
  WrappedEnumGameMode getGamemode(@NonNull TabSlot slot);

  int getPing(@NonNull TabSlot slot);

  boolean canBeReplaced(@NonNull TabEntry entry);
}
