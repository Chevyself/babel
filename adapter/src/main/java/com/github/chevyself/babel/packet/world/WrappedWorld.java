package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedWorld extends ReflectWrapper {

  @NonNull public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("World");

  protected WrappedWorld(Object reference) {
    super(reference);
  }

  @Override
  public Class<?> getReflectClass() {
    return CLAZZ.getWrapped();
  }
}
