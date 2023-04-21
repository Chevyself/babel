package com.github.chevyself.babel.packet.authlib.properties;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.util.ReflectUtil;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wrapper for the Property class. */
public class WrappedProperty extends AbstractWrapper<Object> {

  @NonNull
  static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.properties.Property");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR_TWO =
      WrappedProperty.CLAZZ.getConstructor(String.class, String.class);

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR_THREE =
      WrappedProperty.CLAZZ.getConstructor(String.class, String.class, String.class);

  @NonNull
  private static final WrappedMethod<String> GET_NAME =
      WrappedProperty.CLAZZ.getDeclaredMethod(String.class, "getName");

  @NonNull
  private static final WrappedMethod<String> GET_VALUE =
      WrappedProperty.CLAZZ.getDeclaredMethod(String.class, "getValue");

  @NonNull
  private static final WrappedMethod<String> GET_SIGNATURE =
      WrappedProperty.CLAZZ.getDeclaredMethod(String.class, "getSignature");

  /**
   * Wraps a Property object.
   *
   * @param wrapped the object that must be a Property
   */
  public WrappedProperty(Object wrapped) {
    super(wrapped);
  }

  /**
   * Create a new Property that will be wrapped.
   *
   * @param key the key of the property
   * @param value the value of the property
   * @param signature the signature of the property
   * @return the wrapped property
   * @throws PacketHandlingException if the property could not be constructed
   */
  public static WrappedProperty construct(
      @NonNull String key, @NonNull String value, @NonNull String signature)
      throws PacketHandlingException {
    try {
      return new WrappedProperty(WrappedProperty.CONSTRUCTOR_THREE.invoke(key, value, signature));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Failed to construct Property", e);
    }
  }

  /**
   * Create a new Property that will be wrapped.
   *
   * @param key the key of the property
   * @param value the value of the property
   * @return the wrapped property
   * @throws PacketHandlingException if the property could not be constructed
   */
  public static WrappedProperty construct(@NonNull String key, @NonNull String value)
      throws PacketHandlingException {
    try {
      return new WrappedProperty(WrappedProperty.CONSTRUCTOR_TWO.invoke(key, value));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Failed to construct Property", e);
    }
  }

  /**
   * Get the name of the property.
   *
   * @return the name of the property
   * @throws PacketHandlingException if the name could not be accessed or is null
   */
  @NonNull
  public String getName() throws PacketHandlingException {
    try {
      return ReflectUtil.nonNull(
          WrappedProperty.GET_NAME.prepare(this.wrapped),
          new PacketHandlingException("Cannot access the name of the property " + this));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Cannot access the name of the property " + this, e);
    }
  }

  /**
   * Get the value of the property.
   *
   * @return the value of the property
   * @throws PacketHandlingException if the value could not be accessed or is null
   */
  @NonNull
  public String getValue() throws PacketHandlingException {
    try {
      return ReflectUtil.nonNull(
          WrappedProperty.GET_VALUE.prepare(this.wrapped),
          new PacketHandlingException("Cannot access the value of the property " + this));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Cannot access the value of the property " + this, e);
    }
  }

  /**
   * Get the signature of the property.
   *
   * @return the signature of the property
   * @throws PacketHandlingException if the signature could not be accessed or is null
   */
  @NonNull
  public String getSignature() throws PacketHandlingException {
    try {
      return ReflectUtil.nonNull(
          WrappedProperty.GET_SIGNATURE.prepare(this.wrapped),
          new PacketHandlingException("Cannot access the signature of the property " + this));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Cannot access the signature of the property " + this, e);
    }
  }
}
