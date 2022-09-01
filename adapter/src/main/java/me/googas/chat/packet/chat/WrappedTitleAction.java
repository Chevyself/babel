package me.googas.chat.packet.chat;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.packet.PacketType;
import me.googas.reflect.Wrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;

/** Wraps 'EnumTitleAction' to be used in the {@link PacketType.Play.ClientBound#TITLE}. */
public enum WrappedTitleAction implements Wrapper<Object> {
  TITLE,
  SUBTITLE,
  TIMES,
  CLEAR,
  RESET;

  @NonNull
  private static final WrappedClass<?> TITLE_ACTION =
      WrappedClass.forName(
          PacketType.Play.ClientBound.TITLE.getCanonicalName() + "$EnumTitleAction");

  @NonNull
  private static final WrappedMethod<?> VALUE_OF =
      WrappedTitleAction.TITLE_ACTION.getMethod("valueOf", String.class);

  @Override
  public Object getWrapped() {
    try {
      return WrappedTitleAction.VALUE_OF.invoke(null, this.toString());
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new IllegalStateException("Could not get value of title", e);
    }
  }
}
