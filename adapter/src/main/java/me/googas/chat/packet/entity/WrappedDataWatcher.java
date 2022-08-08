package me.googas.chat.packet.entity;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;

public class WrappedDataWatcher extends SimpleWrapper<Object> {
  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".DataWatcher");

  @NonNull
  public static final WrappedConstructor<?> CONSTRUCTOR =
      CLAZZ.getConstructor(WrappedEntity.CLAZZ.getClazz());

  @NonNull public static final WrappedMethod<?> A = CLAZZ.getMethod("a", int.class, Object.class);

  public WrappedDataWatcher(Object reference) {
    super(reference);
  }

  @NonNull
  public static WrappedDataWatcher construct() {
    try {
      return new WrappedDataWatcher(CONSTRUCTOR.invoke((Object) null));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void a(int i, Object t) throws PacketHandlingException {
    try {
      A.invoke(this.reference, i, t);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke DataWatcher#a", e);
    }
  }
}
