package me.googas.chat.packet.world;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.ReflectWrapper;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedWorld extends ReflectWrapper {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".World");

  protected WrappedWorld(Object reference) {
    super(reference);
  }

  @Override
  public Class<?> getReflectClass() {
    return CLAZZ.getWrapped();
  }
}
