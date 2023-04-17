package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.debug.Debugger;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.logging.Level;
import lombok.NonNull;

public enum WrappedPlayerInfoAction implements Wrapper<Object> {
  REMOVE_PLAYER,
  ADD_PLAYER,
  UPDATE_GAMEMODE,
  UPDATE_LATENCY,
  UPDATE_DISPLAY_NAME;

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName(
          PacketType.Play.ClientBound.PLAYER_INFO.getCanonicalName() + "$EnumPlayerInfoAction");

  @NonNull public static final WrappedMethod<?> VALUE_OF = CLAZZ.getMethod("valueOf", String.class);

  public Object getWrapped() {
    try {
      return VALUE_OF.invoke(null, this.name());
    } catch (InvocationTargetException | IllegalAccessException e) {
      Debugger.getInstance().handle(Level.SEVERE, "Could not get player info action", e);
      return Optional.empty();
    }
  }
}
