package com.github.chevyself.babel.packet.authlib;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.authlib.properties.WrappedPropertyMap;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import lombok.NonNull;

public class WrappedGameProfile extends AbstractWrapper<Object> {

  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.GameProfile");
  public static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedGameProfile.CLAZZ.getConstructor(UUID.class, String.class);
  private static final WrappedMethod<?> GET_PROPERTIES =
      WrappedGameProfile.CLAZZ.getDeclaredMethod("getProperties");

  public WrappedGameProfile(Object wrapped) {
    super(wrapped);
  }

  @NonNull
  public static WrappedGameProfile construct(@NonNull UUID uuid, @NonNull String name)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return new WrappedGameProfile(WrappedGameProfile.CONSTRUCTOR.invoke(uuid, name));
  }

  public WrappedPropertyMap getProperties() throws PacketHandlingException {
    try {
      return new WrappedPropertyMap(WrappedGameProfile.GET_PROPERTIES.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the properties from game profile", e);
    }
  }
}
