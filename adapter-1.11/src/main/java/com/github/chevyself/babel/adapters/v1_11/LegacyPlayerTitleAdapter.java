package com.github.chevyself.babel.adapters.v1_11;

import chevyself.github.commands.bukkit.utils.Components;
import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import com.github.chevyself.babel.debug.Debugger;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.chat.WrappedTitleAction;
import java.util.logging.Level;
import lombok.NonNull;
import org.bukkit.entity.Player;

public final class LegacyPlayerTitleAdapter implements PlayerTitleAdapter {
  @Override
  public void sendTitle(
      @NonNull Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
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
          .handle(Level.SEVERE, "Could not send title, subtitle and times packet", e);
    }
  }
}