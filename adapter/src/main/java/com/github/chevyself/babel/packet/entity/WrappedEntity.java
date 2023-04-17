package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedEntity extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".Entity");

  protected WrappedEntity(Object reference) {
    super(reference);
  }
}
