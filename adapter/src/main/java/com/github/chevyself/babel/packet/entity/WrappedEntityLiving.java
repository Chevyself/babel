package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedEntityLiving extends WrappedEntity {

  @NonNull public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("EntityLiving");

  protected WrappedEntityLiving(Object reference) {
    super(reference);
  }
}
