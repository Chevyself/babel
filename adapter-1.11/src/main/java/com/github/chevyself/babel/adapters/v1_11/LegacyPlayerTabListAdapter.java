package com.github.chevyself.babel.adapters.v1_11;

import chevyself.github.commands.bukkit.utils.Components;
import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import com.github.chevyself.babel.debug.ErrorHandler;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import java.util.logging.Level;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class LegacyPlayerTabListAdapter implements PlayerTabListAdapter {

  @Override
  public void setTabList(@NonNull Player player, @Nullable String header, @Nullable String bottom) {
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
      ErrorHandler.getInstance().handle(Level.SEVERE, "Could not send header_footer packet", e);
    }
  }
}
