package me.googas.chat.packet.entity;

import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;

public class WrappedDataWatcher extends SimpleWrapper<Object> {
  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".DataWatcher");

  private WrappedDataWatcher(Object reference) {
    super(reference);
  }
}
