package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.reflect.AbstractWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import org.bukkit.entity.Player;

/** Wraps the 'CraftPlayer' nms class. */
public final class WrappedCraftPlayer extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CRAFT_PLAYER =
      WrappedClass.forName("org.bukkit.craftbukkit." + Packet.NMS + ".entity.CraftPlayer");

  @NonNull
  private static final WrappedMethod<?> GET_PROFILE =
      WrappedCraftPlayer.CRAFT_PLAYER.getMethod("getProfile");

  @NonNull
  private static final WrappedMethod<?> GET_HANDLE =
      WrappedCraftPlayer.CRAFT_PLAYER.getMethod("getHandle");

  /**
   * Create the wrapper.
   *
   * @param handle the reference of the wrapper
   */
  private WrappedCraftPlayer(Object handle) {
    super(handle);
  }

  /**
   * Wrap a player into its 'CraftPlayer'.
   *
   * @param player the player to wrap
   * @return the wrapped craft player
   */
  @NonNull
  public static WrappedCraftPlayer of(@NonNull Player player) {
    return new WrappedCraftPlayer(player);
  }

  /**
   * Get the wrapped entity player of this craft player.
   *
   * @return the wrapped entity player
   */
  @NonNull
  public WrappedEntityPlayer getHandle() throws PacketHandlingException {
    try {
      return new WrappedEntityPlayer(WrappedCraftPlayer.GET_HANDLE.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the entity from player", e);
    }
  }
}
