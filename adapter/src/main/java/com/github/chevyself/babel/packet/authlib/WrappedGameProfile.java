package com.github.chevyself.babel.packet.authlib;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.authlib.properties.WrappedPropertyMap;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;

/** Wrapper for the GameProfile class. */
public class WrappedGameProfile extends AbstractWrapper<Object> {

  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.GameProfile");
  public static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedGameProfile.CLAZZ.getConstructor(UUID.class, String.class);
  private static final WrappedMethod<?> GET_PROPERTIES =
      WrappedGameProfile.CLAZZ.getDeclaredMethod("getProperties");

  private static final WrappedMethod<UUID> GET_ID =
      WrappedGameProfile.CLAZZ.getDeclaredMethod(UUID.class, "getId");

  /**
   * Creates a new wrapper for the GameProfile class.
   *
   * @param wrapped the object that must be a GameProfile
   */
  public WrappedGameProfile(Object wrapped) {
    super(wrapped);
  }

  /**
   * Constructs a new GameProfile object and wraps it.
   *
   * @param uuid the uuid of the profile
   * @param name the name of the profile
   * @return the wrapped game profile
   * @throws PacketHandlingException if the game profile could not be constructed
   */
  @NonNull
  public static WrappedGameProfile construct(@NonNull UUID uuid, @NonNull String name)
      throws PacketHandlingException {
    try {
      return new WrappedGameProfile(WrappedGameProfile.CONSTRUCTOR.invoke(uuid, name));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not construct game profile", e);
    }
  }

  /**
   * Get the property map and wrap it.
   *
   * @return the wrapped property map
   * @throws PacketHandlingException if the properties could not be retrieved
   */
  @NonNull
  public WrappedPropertyMap getProperties() throws PacketHandlingException {
    try {
      return new WrappedPropertyMap(WrappedGameProfile.GET_PROPERTIES.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the properties from game profile", e);
    }
  }

  /**
   * Get the unique id of the profile.
   *
   * @return the uuid
   */
  @NonNull
  public UUID getId() {
    UUID uuid = UUID.randomUUID();
    try {
      uuid = WrappedGameProfile.GET_ID.prepare(this.wrapped);
    } catch (InvocationTargetException | IllegalAccessException e) {
      Debugger.getInstance()
          .getLogger()
          .log(Level.SEVERE, "Could not get the id from game profile", e);
    }
    return uuid == null ? UUID.randomUUID() : uuid;
  }
}
