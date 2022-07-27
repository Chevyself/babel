package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;

/** Wraps the 'PlayerConnection' nms class. */
public class WrappedPlayerConnection extends SimpleWrapper<Object> {

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
    if (packet.get().isPresent()) {
      Object handle = packet.get().orElse(null);
      if (handle != null) {
        try {
          WrappedPlayerConnection.SEND_PACKET.invoke(this.reference, handle);
        } catch (InvocationTargetException | IllegalAccessException e) {
          throw new PacketHandlingException("Could not invoke method to send packet", e);
        }
      }
    }
  }
}
