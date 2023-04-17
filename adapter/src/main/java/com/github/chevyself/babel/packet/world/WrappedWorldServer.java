package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedWorldServer extends WrappedWorld {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".WorldServer");

  WrappedWorldServer(Object reference) {
    super(reference);
  }
}
