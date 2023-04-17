package me.googas.chat.adapters.v1_11;

import java.util.logging.Level;

import chevyself.github.commands.bukkit.utils.Components;
import lombok.NonNull;
import me.googas.chat.adapters.PlayerTabListAdapter;
import me.googas.chat.debug.Debugger;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.chat.WrappedChatComponent;
import org.bukkit.entity.Player;

public final class LegacyPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, String header, String bottom) {
    try {
      PacketType.Play.ClientBound.HEADER_FOOTER
          .create()
          .setField(
              2,
              header == null
                  ? null
                  : WrappedChatComponent.of(Components.deserializePlain('&', header)))
          .setField(
              3,
              bottom == null
                  ? null
                  : WrappedChatComponent.of(Components.deserializePlain('&', bottom)))
          .send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().handle(Level.SEVERE, "Could not send header_footer packet", e);
    }
  }
}
