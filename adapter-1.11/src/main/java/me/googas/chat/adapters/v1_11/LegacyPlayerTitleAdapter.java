package me.googas.chat.adapters.v1_11;

import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.adapters.PlayerTitleAdapter;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.chat.WrappedChatComponent;
import me.googas.chat.packet.chat.WrappedTitleAction;
import me.googas.commands.bukkit.utils.Components;
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
      ErrorHandler.getInstance()
          .handle(Level.SEVERE, "Could not send title, subtitle and times packet", e);
    }
  }
}
