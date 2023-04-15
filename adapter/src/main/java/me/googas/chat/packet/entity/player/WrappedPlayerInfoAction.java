package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.debug.Debugger;
import me.googas.chat.packet.PacketType;
import me.googas.reflect.Wrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;

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
