package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

/** Wrapper for the nms World class. */
public class WrappedWorld extends ReflectWrapper {

  @NonNull
  public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("world.level", "World");

  /**
   * Create a new WrappedWorld.
   *
   * @param reference the object that must be a World
   */
  protected WrappedWorld(Object reference) {
    super(reference);
  }

  @Override
  public Class<?> getReflectClass() {
    return WrappedWorld.CLAZZ.getWrapped();
  }
}
