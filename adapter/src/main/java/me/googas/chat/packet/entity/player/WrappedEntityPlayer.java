package me.googas.chat.packet.entity.player;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.chat.WrappedChatComponent;
import me.googas.chat.packet.properties.WrappedProperty;
import me.googas.reflect.AbstractWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedField;
import me.googas.reflect.wrappers.WrappedMethod;

/** Wraps the 'EntityPlayer' nms class. */
public final class WrappedEntityPlayer extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> ENTITY_PLAYER =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".EntityPlayer");

  @NonNull
  private static final WrappedMethod<?> GET_PROFILE = ENTITY_PLAYER.getMethod("getProfile");

  @NonNull
  private static final WrappedMethod<?> GET_PLAYER_LIST_NAME =
      ENTITY_PLAYER.getMethod("getPlayerListName");

  @NonNull
  private static final WrappedField<?> PLAYER_CONNECTION =
      WrappedEntityPlayer.ENTITY_PLAYER.getDeclaredField("playerConnection");

  @NonNull
  private static final WrappedField<?> PLAYER_INTERACT_MANAGER =
      ENTITY_PLAYER.getDeclaredField("playerInteractManager");

  @NonNull
  private static final WrappedField<Integer> PING =
      WrappedEntityPlayer.ENTITY_PLAYER.getDeclaredField(int.class, "ping");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedEntityPlayer(Object reference) {
    super(reference);
  }

  public static Object createArray(int size) {
    return Array.newInstance(ENTITY_PLAYER.getClazz(), size);
  }

  /**
   * Get the wrapped player connection of this player.
   *
   * @return the wrapped player connection
   * @throws NullPointerException if the connection could not be provided
   */
  @NonNull
  public WrappedPlayerConnection playerConnection() throws PacketHandlingException {
    try {
      return new WrappedPlayerConnection(
          WrappedEntityPlayer.PLAYER_CONNECTION.provide(this.wrapped));
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the player connection", e);
    }
  }

  public WrappedPlayerInfo playerInfo(@NonNull Packet packet)
      throws InvocationTargetException, IllegalAccessException, PacketHandlingException {
    int ping = (int) PING.provide(this.wrapped);
    return WrappedPlayerInfo.construct(
        packet,
        this.getProfile(),
        ping,
        this.playerInteractManager().getGameMode(),
        getPlayerListName());
  }

  @NonNull
  private WrappedPlayerInteractManager playerInteractManager() throws IllegalAccessException {
    return new WrappedPlayerInteractManager(PLAYER_INTERACT_MANAGER.provide(this.wrapped));
  }

  @NonNull
  private WrappedChatComponent getPlayerListName()
      throws InvocationTargetException, IllegalAccessException {
    return new WrappedChatComponent(GET_PLAYER_LIST_NAME.invoke(this.wrapped));
  }

  @NonNull
  public WrappedGameProfile getProfile() throws InvocationTargetException, IllegalAccessException {
    return new WrappedGameProfile(GET_PROFILE.invoke(this.wrapped));
  }

  public Skin getSkin() throws InvocationTargetException, IllegalAccessException {
    for (WrappedProperty property : getProfile().getProperties().get("textures")) {
      if (property.getName().equals("textures")) {
        return new Skin(property.getValue(), property.getSignature());
      }
    }
    return null;
  }
}
