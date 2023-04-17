package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

public class WrappedPlayerInfo extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName(
          PacketType.Play.ClientBound.PLAYER_INFO.getCanonicalName() + "$PlayerInfoData");

  @NonNull
  public static final WrappedConstructor<?> CONSTRUCTOR =
      CLAZZ.getConstructor(
          PacketType.Play.ClientBound.PLAYER_INFO.wrap().getClazz(),
          WrappedGameProfile.CLAZZ.getClazz(),
          int.class,
          WrappedEnumGameMode.CLAZZ.getClazz(),
          WrappedChatComponent.CLAZZ.getClazz());

  public WrappedPlayerInfo(Object handle) {
    super(handle);
  }

  public static @NonNull WrappedPlayerInfo construct(
      @NonNull Packet packet,
      @NonNull WrappedGameProfile profile,
      int ping,
      @NonNull WrappedEnumGameMode gamemode,
      @NonNull WrappedChatComponent display)
      throws PacketHandlingException {
    try {
      return new WrappedPlayerInfo(
          CONSTRUCTOR.invoke(
              packet.getWrapped(),
              profile.getWrapped(),
              ping,
              gamemode.getWrapped(),
              display.getWrapped()));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create PlayerInfo");
    }
  }
}
