package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.authlib.WrappedGameProfile;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.entity.WrappedPublicKeyData;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps the 'PlayerInfoData' nms class. */
public class WrappedPlayerInfo extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName(
          PacketType.Play.ClientBound.PLAYER_INFO.getCanonicalName() + "$PlayerInfoData");

  // TODO a better way to approach constructors
  @NonNull public static final WrappedConstructor<?> CONSTRUCTOR;

  static {
    if (Versions.BUKKIT < 17) {
      CONSTRUCTOR =
          WrappedPlayerInfo.CLAZZ.getConstructor(
              PacketType.Play.ClientBound.PLAYER_INFO.wrap().getClazz(),
              WrappedGameProfile.CLAZZ.getClazz(),
              int.class,
              WrappedEnumGameMode.CLAZZ.getClazz(),
              WrappedChatComponent.CLAZZ.getClazz());
    } else if (Versions.BUKKIT >= 19) {
      CONSTRUCTOR =
          WrappedPlayerInfo.CLAZZ.getConstructor(
              WrappedGameProfile.CLAZZ.getClazz(),
              int.class,
              WrappedEnumGameMode.CLAZZ.getClazz(),
              WrappedChatComponent.CLAZZ.getClazz(),
              WrappedPublicKeyData.CLAZZ.getClazz());
    } else {
      CONSTRUCTOR =
          WrappedPlayerInfo.CLAZZ.getConstructor(
              WrappedGameProfile.CLAZZ.getClazz(),
              int.class,
              WrappedEnumGameMode.CLAZZ.getClazz(),
              WrappedChatComponent.CLAZZ.getClazz());
    }
  }

  /**
   * Wraps a PlayerInfoData object.
   *
   * @param handle the object that must be a PlayerInfoData
   */
  public WrappedPlayerInfo(Object handle) {
    super(handle);
  }

  /**
   * Creates a new PlayerInfoData object.
   *
   * @param packet the packet that contains the PlayerInfoData
   * @param profile the profile of the player
   * @param ping the ping of the player
   * @param gamemode the gamemode of the player
   * @param display the display name of the player
   * @return the new PlayerInfoData object
   * @throws PacketHandlingException if the object could not be created
   */
  public static @NonNull WrappedPlayerInfo construct(
      @NonNull Packet packet,
      @NonNull WrappedGameProfile profile,
      int ping,
      @NonNull WrappedEnumGameMode gamemode,
      @NonNull WrappedChatComponent display)
      throws PacketHandlingException {
    try {
      if (Versions.BUKKIT < 17) {
        return new WrappedPlayerInfo(
            WrappedPlayerInfo.CONSTRUCTOR.invoke(
                packet.getWrapped(),
                profile.getWrapped(),
                ping,
                gamemode.getWrapped(),
                display.getWrapped()));
      } else if (Versions.BUKKIT >= 19) {
        return new WrappedPlayerInfo(
            WrappedPlayerInfo.CONSTRUCTOR.invoke(
                profile.getWrapped(), ping, gamemode.getWrapped(), display.getWrapped(), null));
      } else {
        return new WrappedPlayerInfo(
            WrappedPlayerInfo.CONSTRUCTOR.invoke(
                profile.getWrapped(), ping, gamemode.getWrapped(), display.getWrapped()));
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create PlayerInfo");
    }
  }
}
