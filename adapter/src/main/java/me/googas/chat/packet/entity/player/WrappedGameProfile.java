package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.packet.properties.WrappedPropertyMap;
import me.googas.reflect.AbstractWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;

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
