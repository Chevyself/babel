package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedEntity extends AbstractWrapper<Object> {

  @NonNull public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("Entity");

  protected WrappedEntity(Object reference) {
    super(reference);
  }
}
