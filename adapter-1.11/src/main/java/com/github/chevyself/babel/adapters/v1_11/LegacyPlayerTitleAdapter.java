package com.github.chevyself.babel.adapters.v1_11;

import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.chat.WrappedTitleAction;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.starbox.bukkit.utils.Components;
import java.util.logging.Level;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/** This class is used to send titles to players on 1.11 or lower. */
public final class LegacyPlayerTitleAdapter implements PlayerTitleAdapter {
  @Override
  public void sendTitle(
      @NonNull Player player,
      @Nullable String title,
      @Nullable String subtitle,
      int fadeIn,
      int stay,
      int fadeOut) {
    PacketType type = PacketType.Play.ClientBound.TITLE;
    try {
      if (title != null) {
        type.create()
            .setField(0, WrappedTitleAction.TITLE)
            .setField(1, WrappedChatComponent.of(Components.deserializePlain('&', title)))
            .setField(2, -1)
            .setField(3, -1)
            .setField(4, -1)
            .send(player);
      }
      if (subtitle != null) {
        type.create()
            .setField(0, WrappedTitleAction.SUBTITLE)
            .setField(1, WrappedChatComponent.of(Components.deserializePlain('&', subtitle)))
            .setField(2, -1)
            .setField(3, -1)
            .setField(4, -1)
            .send(player);
      }
      type.create()
          .setField(0, WrappedTitleAction.TIMES)
          .setField(2, fadeIn)
          .setField(3, stay)
          .setField(4, fadeOut)
          .send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance()
          .getLogger()
          .log(Level.SEVERE, "Could not send title, subtitle and times packet", e);
    }
  }
}
