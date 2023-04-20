package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.properties.WrappedProperty;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps the 'EntityPlayer' nms class. */
public final class WrappedEntityPlayer extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> ENTITY_PLAYER =
      Versions.wrapNmsClassByName("server.level", "EntityPlayer");

  @NonNull
  private static final WrappedMethod<?> GET_PROFILE =
      LookUp.methodOn(WrappedEntityPlayer.ENTITY_PLAYER)
          .since(8, "getProfile")
          .since(18, "fp")
          .since(18, 2, "fq")
          .since(19, "fz")
          .since(19, 1, "fy")
          .since(19, 3, "fD")
          .since(19, 4, "fI")
          .find();

  @NonNull
  private static final WrappedMethod<?> GET_PLAYER_LIST_NAME =
      LookUp.methodOn(WrappedEntityPlayer.ENTITY_PLAYER)
              .since(8, "getPlayerListName")
              .since(18, "J")
              .since(19, 3, "K")
              .since(19, 4, "J")
                  .find();

  @NonNull private static final WrappedField<?> PLAYER_CONNECTION =
      LookUp.fieldOn(WrappedEntityPlayer.ENTITY_PLAYER)
          .since(8, "playerConnection")
          .since(16, "b")
          .find();

  @NonNull
  private static final WrappedField<?> PLAYER_INTERACT_MANAGER =
      LookUp.fieldOn(WrappedEntityPlayer.ENTITY_PLAYER)
          .since(8, "playerInteractManager")
          .since(17, "d")
          .find();

  @NonNull
  private static final WrappedField<Integer> PING =
      LookUp.fieldOn(WrappedEntityPlayer.ENTITY_PLAYER, int.class)
          .since(8, "ping")
          .since(17, "e")
          .find();

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedEntityPlayer(Object reference) {
    super(reference);
  }

  public static Object createArray(int size) {
    return Array.newInstance(WrappedEntityPlayer.ENTITY_PLAYER.getClazz(), size);
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
    int ping = (int) WrappedEntityPlayer.PING.provide(this.wrapped);
    return WrappedPlayerInfo.construct(
        packet,
        this.getProfile(),
        ping,
        this.playerInteractManager().getGameMode(),
        this.getPlayerListName());
  }

  @NonNull
  private WrappedPlayerInteractManager playerInteractManager() throws IllegalAccessException {
    return new WrappedPlayerInteractManager(WrappedEntityPlayer.PLAYER_INTERACT_MANAGER.provide(this.wrapped));
  }

  @NonNull
  private WrappedChatComponent getPlayerListName()
      throws InvocationTargetException, IllegalAccessException {
    return new WrappedChatComponent(WrappedEntityPlayer.GET_PLAYER_LIST_NAME.invoke(this.wrapped));
  }

  @NonNull
  public WrappedGameProfile getProfile() throws InvocationTargetException, IllegalAccessException {
    return new WrappedGameProfile(WrappedEntityPlayer.GET_PROFILE.invoke(this.wrapped));
  }

  public Skin getSkin() throws InvocationTargetException, IllegalAccessException {
    for (WrappedProperty property : this.getProfile().getProperties().get("textures")) {
      if (property.getName().equals("textures")) {
        return new Skin(property.getValue(), property.getSignature());
      }
    }
    return null;
  }
}
