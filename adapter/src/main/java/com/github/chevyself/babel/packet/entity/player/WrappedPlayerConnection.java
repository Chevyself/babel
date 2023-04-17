package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps the 'PlayerConnection' nms class. */
public class WrappedPlayerConnection extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> ENTITY_PLAYER =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".PlayerConnection");

  @NonNull
  private static final WrappedMethod<?> SEND_PACKET =
      WrappedPlayerConnection.ENTITY_PLAYER.getMethod("sendPacket", Packet.PACKET_CLASS.getClazz());

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedPlayerConnection(Object reference) {
    super(reference);
  }

  /**
   * Send a packet to this client connection.
   *
   * @param packet the packet to be sent
   */
  public void sendPacket(@NonNull Packet packet) throws PacketHandlingException {
    if (packet.isPresent()) {
      Object handle = packet.getWrapped();
      if (handle != null) {
        try {
          WrappedPlayerConnection.SEND_PACKET.invoke(this.wrapped, handle);
        } catch (InvocationTargetException | IllegalAccessException e) {
          throw new PacketHandlingException("Could not invoke method to send packet", e);
        }
      }
    }
  }
}
