package me.googas.chat.packet.world;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedWorldServer extends WrappedWorld {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".WorldServer");

  WrappedWorldServer(Object reference) {
    super(reference);
  }
}
