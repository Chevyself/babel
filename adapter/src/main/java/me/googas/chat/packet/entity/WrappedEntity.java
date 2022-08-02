package me.googas.chat.packet.entity;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedEntity extends SimpleWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".Entity");

  protected WrappedEntity(Object reference) {
    super(reference);
  }
}
