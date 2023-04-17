package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

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
