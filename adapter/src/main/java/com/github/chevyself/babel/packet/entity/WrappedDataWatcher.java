package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

public class WrappedDataWatcher extends AbstractWrapper<Object> {
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
      A.invoke(this.wrapped, i, t);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke DataWatcher#a", e);
    }
  }
}
