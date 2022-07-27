package me.googas.chat.packet.entity.player;

import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedField;

/** Wraps the 'EntityPlayer' nms class. */
public final class WrappedEntityPlayer extends SimpleWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> ENTITY_PLAYER =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".EntityPlayer");

  @NonNull
  private static final WrappedField<?> PLAYER_CONNECTION =
      WrappedEntityPlayer.ENTITY_PLAYER.getDeclaredField("playerConnection");

  @NonNull
  private static final WrappedField<Integer> PING =
      WrappedEntityPlayer.ENTITY_PLAYER.getDeclaredField(int.class, "ping");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedEntityPlayer(Object reference) {
    super(reference);
  }

  /**
   * Get the wrapped player connection of this player.
   *
   * @return the wrapped player connection
   * @throws NullPointerException if the connection could not be provided
   */
  @NonNull
  public WrappedPlayerConnection playerConnection() throws PacketHandlingException {
    try {
      return new WrappedPlayerConnection(WrappedEntityPlayer.PLAYER_CONNECTION.provide(reference));
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the player connection", e);
    }
  }
}
