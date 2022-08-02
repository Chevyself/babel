package me.googas.chat.packet.world;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedWorld extends SimpleWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".World");

  WrappedWorld(Object reference) {
    super(reference);
  }
}
