package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps the 'PlayerInteractManager' nms class. */
public class WrappedPlayerInteractManager extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CLAZZ =
      Versions.wrapNmsClassByName("server.level", "PlayerInteractManager");

  @NonNull
  private static final WrappedMethod<?> GET_GAMEMODE =
      WrappedPlayerInteractManager.CLAZZ.getMethod("getGameMode");

  /**
   * Creates a new wrapped PlayerInteractManager.
   *
   * @param handle the object that must be a PlayerInteractManager
   */
  public WrappedPlayerInteractManager(Object handle) {
    super(handle);
  }

  /**
   * Get the game mode of the player.
   *
   * @return the game mode
   * @throws PacketHandlingException if an error occurs while invoking the method
   */
  @NonNull
  public WrappedEnumGameMode getGameMode() throws PacketHandlingException {
    try {
      return WrappedEnumGameMode.valueOf(
          WrappedPlayerInteractManager.GET_GAMEMODE.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke method to get game mode", e);
    }
  }
}
