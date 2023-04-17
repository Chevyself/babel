package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedEntityLiving extends WrappedEntity {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".EntityLiving");

  protected WrappedEntityLiving(Object reference) {
    super(reference);
  }
}
