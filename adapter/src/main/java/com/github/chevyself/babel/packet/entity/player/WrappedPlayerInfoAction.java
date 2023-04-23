package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Wraps the 'EnumPlayerInfoAction' nms class. */
public enum WrappedPlayerInfoAction implements Wrapper<Object> {
  ADD_PLAYER,
  INITIALIZE_CHAT,

  UPDATE_GAMEMODE,
  UPDATE_LISTED,

  UPDATE_LATENCY,
  UPDATE_DISPLAY_NAME,
  /**
   * Removes the player from the tab-list.
   *
   * @deprecated removed in 1.19.2
   */
  REMOVE_PLAYER;

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      LookUp.forClass()
          .since(
              8,
              PacketType.Play.ClientBound.PLAYER_INFO.getCanonicalName() + "$EnumPlayerInfoAction")
          .since(19, 2, PacketType.Play.ClientBound.PLAYER_INFO_UPDATE.getCanonicalName() + "$a")
          .find();

  @NonNull
  public static final WrappedMethod<?> VALUE_OF =
      WrappedPlayerInfoAction.CLAZZ.getMethod("valueOf", String.class);

  @Nullable
  public Object getWrapped() {
    try {
      return WrappedPlayerInfoAction.VALUE_OF.invoke(null, this.name());
    } catch (InvocationTargetException | IllegalAccessException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not get player info action", e);
      return null;
    }
  }
}
