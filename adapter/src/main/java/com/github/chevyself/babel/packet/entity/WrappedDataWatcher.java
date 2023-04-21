package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps a DataWatcher object. */
public class WrappedDataWatcher extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      Versions.wrapNmsClassByName("network.syncher", "DataWatcher");

  @NonNull
  public static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedDataWatcher.CLAZZ.getConstructor(WrappedEntity.CLAZZ.getClazz());

  @NonNull
  public static final WrappedMethod<?> A =
      WrappedDataWatcher.CLAZZ.getMethod("a", int.class, Object.class);

  /**
   * Wraps a DataWatcher object.
   *
   * @param reference the reference object that must be a DataWatcher
   */
  public WrappedDataWatcher(Object reference) {
    super(reference);
  }

  /**
   * Constructs a new DataWatcher object.
   *
   * @return a new DataWatcher object
   * @throws PacketHandlingException if the DataWatcher could not be constructed
   */
  @NonNull
  public static WrappedDataWatcher construct() throws PacketHandlingException {
    try {
      return new WrappedDataWatcher(WrappedDataWatcher.CONSTRUCTOR.invoke((Object) null));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not construct DataWatcher", e);
    }
  }

  /**
   * Sets a value in the DataWatcher.
   *
   * @deprecated removed in 1.9.
   * @param i the index of the value
   * @param t the value
   * @throws PacketHandlingException if the value could not be set
   */
  public void a(int i, Object t) throws PacketHandlingException {
    try {
      WrappedDataWatcher.A.invoke(this.wrapped, i, t);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke DataWatcher#a", e);
    }
  }
}
