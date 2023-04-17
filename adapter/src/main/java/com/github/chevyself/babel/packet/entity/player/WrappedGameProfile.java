package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.packet.properties.WrappedPropertyMap;
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
      CLAZZ.getConstructor(UUID.class, String.class);
  private static final WrappedMethod<?> GET_PROPERTIES = CLAZZ.getDeclaredMethod("getProperties");

  public WrappedGameProfile(Object wrapped) {
    super(wrapped);
  }

  @NonNull
  public static WrappedGameProfile construct(@NonNull UUID uuid, @NonNull String name)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return new WrappedGameProfile(CONSTRUCTOR.invoke(uuid, name));
  }

  public WrappedPropertyMap getProperties()
      throws InvocationTargetException, IllegalAccessException {
    return new WrappedPropertyMap(GET_PROPERTIES.invoke(this.wrapped));
  }
}
