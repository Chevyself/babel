package com.github.chevyself.babel.packet.craft;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.entity.player.WrappedEntityPlayer;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
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
   * @throws PacketHandlingException if the entity player could not be retrieved
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
