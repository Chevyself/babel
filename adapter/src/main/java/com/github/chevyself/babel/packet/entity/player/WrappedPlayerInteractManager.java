package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

public class WrappedPlayerInteractManager extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("PlayerInteractManager");

  @NonNull private static final WrappedMethod<?> GET_GAMEMODE = CLAZZ.getMethod("getGameMode");

  public WrappedPlayerInteractManager(Object handle) {
    super(handle);
  }

  public WrappedEnumGameMode getGameMode()
      throws InvocationTargetException, IllegalAccessException {
    return WrappedEnumGameMode.valueOf(GET_GAMEMODE.invoke(this.wrapped));
  }
}
