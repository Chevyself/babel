package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.authlib.WrappedGameProfile;
import com.github.chevyself.babel.packet.authlib.properties.WrappedProperty;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.entity.WrappedEntityLiving;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.util.ReflectUtil;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Wraps the 'EntityPlayer' nms class. */
public final class WrappedEntityPlayer extends WrappedEntityLiving {

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

  @NonNull
  private static final WrappedField<?> PLAYER_CONNECTION =
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
      LookUp.fieldOn(WrappedEntityPlayer.ENTITY_PLAYER, Integer.class)
          .since(8, "ping")
          .since(17, "e")
          .find();

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedEntityPlayer(Object reference) {
    super(reference);
  }

  /**
   * Create an array of entity players.
   *
   * @param size the size of the array
   * @return the array
   */
  public static Object createArray(int size) {
    return Array.newInstance(WrappedEntityPlayer.ENTITY_PLAYER.getClazz(), size);
  }

  /**
   * Get the wrapped player connection of this player.
   *
   * @return the wrapped player connection
   * @throws NullPointerException if the connection could not be provided
   * @throws PacketHandlingException if the connection could not be provided
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

  /**
   * Get this entity player as a player info.
   *
   * @param packet the packet to construct the player info with
   * @return the player info
   * @throws PacketHandlingException if the player info could not be constructed
   */
  @NonNull
  public WrappedPlayerInfo playerInfo(@Nullable Packet packet) throws PacketHandlingException {
    try {
      int ping = ReflectUtil.safelyUnbox(WrappedEntityPlayer.PING.get(this.wrapped), 0);
      return WrappedPlayerInfo.construct(
          packet,
          this.getProfile(),
          ping,
          this.playerInteractManager().getGameMode(),
          this.getPlayerListName());
    } catch (PacketHandlingException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the player info", e);
    }
  }

  @NonNull
  private WrappedPlayerInteractManager playerInteractManager() throws PacketHandlingException {
    try {
      return new WrappedPlayerInteractManager(
          WrappedEntityPlayer.PLAYER_INTERACT_MANAGER.provide(this.wrapped));
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the player interact manager", e);
    }
  }

  @NonNull
  private WrappedChatComponent getPlayerListName() throws PacketHandlingException {
    try {
      return new WrappedChatComponent(
          WrappedEntityPlayer.GET_PLAYER_LIST_NAME.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the player list name", e);
    }
  }

  /**
   * Get the game profile of this player.
   *
   * @return the game profile
   * @throws PacketHandlingException if the game profile could not be provided
   */
  @NonNull
  public WrappedGameProfile getProfile() throws PacketHandlingException {
    try {
      return new WrappedGameProfile(WrappedEntityPlayer.GET_PROFILE.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the game profile", e);
    }
  }

  /**
   * Get the skin of this player.
   *
   * @return the skin
   * @throws PacketHandlingException if the skin could not be provided
   */
  public Skin getSkin() throws PacketHandlingException {
    for (WrappedProperty property : this.getProfile().getProperties().get("textures")) {
      if (property.getName().equals("textures")) {
        return new Skin(property.getValue(), property.getSignature());
      }
    }
    return null;
  }
}
