package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.chat.WrappedChatComponent;
import me.googas.chat.packet.world.WrappedEnumGameMode;
import me.googas.reflect.AbstractWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;

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
