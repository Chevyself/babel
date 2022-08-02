package me.googas.chat.packet.entity;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedEntityLiving extends WrappedEntity {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".EntityLiving");

  protected WrappedEntityLiving(Object reference) {
    super(reference);
  }
}
