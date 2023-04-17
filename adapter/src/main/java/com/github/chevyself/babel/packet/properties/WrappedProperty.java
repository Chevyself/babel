package com.github.chevyself.babel.packet.properties;

import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

public class WrappedProperty extends AbstractWrapper<Object> {

  @NonNull
  static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.properties.Property");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR_TWO =
      CLAZZ.getConstructor(String.class, String.class);

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR_THREE =
      CLAZZ.getConstructor(String.class, String.class, String.class);

  @NonNull
  private static final WrappedMethod<String> GET_NAME =
      CLAZZ.getDeclaredMethod(String.class, "getName");

  @NonNull
  private static final WrappedMethod<String> GET_VALUE =
      CLAZZ.getDeclaredMethod(String.class, "getValue");

  @NonNull
  private static final WrappedMethod<String> GET_SIGNATURE =
      CLAZZ.getDeclaredMethod(String.class, "getSignature");

  public WrappedProperty(Object wrapped) {
    super(wrapped);
  }

  public static WrappedProperty construct(
      @NonNull String key, @NonNull String value, @NonNull String signature)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return new WrappedProperty(CONSTRUCTOR_THREE.invoke(key, value, signature));
  }

  public static WrappedProperty construct(@NonNull String key, @NonNull String value)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return new WrappedProperty(CONSTRUCTOR_TWO.invoke(key, value));
  }

  @NonNull
  public String getName() throws InvocationTargetException, IllegalAccessException {
    return GET_NAME.prepare(this.wrapped);
  }

  @NonNull
  public String getValue() throws InvocationTargetException, IllegalAccessException {
    return GET_VALUE.prepare(this.wrapped);
  }

  @NonNull
  public String getSignature() throws InvocationTargetException, IllegalAccessException {
    return GET_SIGNATURE.prepare(this.wrapped);
  }
}
