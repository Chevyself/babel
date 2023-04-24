package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.babel.packet.authlib.WrappedGameProfile;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wraps the 'PlayerInfoUpdate$b' nms class. This class is mapped to
 * 'net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket$Entry'
 */
public class WrappedPlayerInfoEntry extends ReflectWrapper {

  @NonNull
  private static final WrappedClass<?> CLAZZ =
      WrappedClass.forName(
          PacketType.Play.ClientBound.PLAYER_INFO_UPDATE.getCanonicalName() + "$b");

  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedPlayerInfoEntry.CLAZZ.getConstructor(
          UUID.class,
          WrappedGameProfile.CLAZZ.getClazz(),
          boolean.class,
          int.class,
          WrappedEnumGameMode.CLAZZ.getClazz(),
          WrappedChatComponent.CLAZZ.getClazz(),
          WrappedRemoteChatSessionData.CLAZZ.getClazz());

  private WrappedPlayerInfoEntry(Object toWrap) {
    super(toWrap, true);
  }

  /**
   * Constructs a player info entry and wraps it.
   *
   * @param profileId the profile id
   * @param profile the game profile
   * @param listed whether the player is listed in the tab-list
   * @param ping the player's ping
   * @param gameMode the player's game mode
   * @param displayName the player's display name
   * @param chatSession the player's chat session
   * @return the wrapped player info entry
   */
  public static WrappedPlayerInfoEntry construct(
      @NonNull UUID profileId,
      @NonNull WrappedGameProfile profile,
      boolean listed,
      int ping,
      @NonNull WrappedEnumGameMode gameMode,
      @Nullable WrappedChatComponent displayName,
      @Nullable WrappedRemoteChatSessionData chatSession) {
    Object toWrap = null;
    try {
      toWrap =
          WrappedPlayerInfoEntry.CONSTRUCTOR.invoke(
              profileId,
              profile.getWrapped(),
              listed,
              ping,
              gameMode.getWrapped(),
              displayName == null ? null : displayName.getWrapped(),
              chatSession == null ? null : chatSession.getWrapped());
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      Debugger.getInstance()
          .getLogger()
          .log(Level.SEVERE, "Could not construct player info entry", e);
    }
    return new WrappedPlayerInfoEntry(toWrap);
  }

  @Override
  public Class<?> getReflectClass() {
    return WrappedPlayerInfoEntry.CLAZZ.getClazz();
  }
}
