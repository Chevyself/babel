package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.world.WrappedEnumGameMode;
import me.googas.reflect.AbstractWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;

public class WrappedPlayerInteractManager extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".PlayerInteractManager");

  @NonNull private static final WrappedMethod<?> GET_GAMEMODE = CLAZZ.getMethod("getGameMode");

  public WrappedPlayerInteractManager(Object handle) {
    super(handle);
  }

  public WrappedEnumGameMode getGameMode()
      throws InvocationTargetException, IllegalAccessException {
    return WrappedEnumGameMode.valueOf(GET_GAMEMODE.invoke(this.wrapped));
  }
}
