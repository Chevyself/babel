package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
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
   * <p>In 1.19.3 and up a new packet was created to remove players from the tab-list: {@link PacketType.Play.ClientBound#PLAYER_INFO_REMOVE}
   *
   * @deprecated removed in 1.19.3
   */
  REMOVE_PLAYER;

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      LookUp.forClass()
          .since(8, PacketType.Play.ClientBound.PLAYER_INFO.getName() + "$EnumPlayerInfoAction")
          .since(19, 3, PacketType.Play.ClientBound.PLAYER_INFO_UPDATE.getName() + "$a")
          .find();

  @NonNull
  public static final WrappedMethod<?> VALUE_OF =
      WrappedPlayerInfoAction.CLAZZ.getMethod("valueOf", String.class);

  @SuppressWarnings("rawtypes")
  @NonNull
  private static final WrappedMethod<EnumSet> OF =
      WrappedClass.of(EnumSet.class).getMethod(EnumSet.class, "of", Enum.class);

  @SuppressWarnings("rawtypes")
  @NonNull
  private static final WrappedMethod<EnumSet> COPY_OF =
      WrappedClass.of(EnumSet.class).getMethod(EnumSet.class, "copyOf", Collection.class);

  @Nullable
  public Object getWrapped() {
    try {
      return WrappedPlayerInfoAction.VALUE_OF.invoke(null, this.name());
    } catch (InvocationTargetException | IllegalAccessException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not get player info action", e);
      return null;
    }
  }

  /**
   * Get the action as an enum set.
   *
   * @param actions the action to get as an enum set
   * @return the actions as an enum set
   * @throws PacketHandlingException if the enum set could not be created
   * @param action the action to get as an enum set
   */
  public static EnumSet<?> toEnumSet(@NonNull WrappedPlayerInfoAction action)
      throws PacketHandlingException {
    try {
      EnumSet<?> enumSet = WrappedClass.of(EnumSet.class)
          .getMethod(EnumSet.class, "of", Enum.class)
          .prepare(null, action.getWrapped());
      return Objects.requireNonNull(enumSet);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create enum set", e);
    }
  }

  /**
   * Get the actions as an enum set.
   *
   * @param actions the actions to get as an enum set
   * @return the actions as an enum set
   * @throws PacketHandlingException if the enum set could not be created
   */
  public static EnumSet<?> toEnumSet(@NonNull WrappedPlayerInfoAction... actions)
      throws PacketHandlingException {
    if (actions.length == 0) {
      throw new IllegalArgumentException("Cannot create enum set from empty array");
    }
    List<Object> entries =
        Arrays.stream(actions)
            .map(WrappedPlayerInfoAction::getWrapped)
            .collect(Collectors.toList());
    try {
      EnumSet<?> enumSet = WrappedPlayerInfoAction.COPY_OF
          .prepare(null, entries);
      return Objects.requireNonNull(enumSet);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create enum set", e);
    }
  }

  /**
   * Get this action as an enum set.
   *
   * @return the action as an enum set
   * @throws PacketHandlingException if the enum set could not be created
   */
  @NonNull
  public EnumSet<?> toEnumSet() throws PacketHandlingException {
    return WrappedPlayerInfoAction.toEnumSet(this);
  }
}
