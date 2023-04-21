package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.world.WrappedWorld;
import com.github.chevyself.babel.packet.world.WrappedWorldServer;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

public class WrappedEntityWither extends WrappedEntityLiving {

  @NonNull
  private static final WrappedClass<?> ENTITY_WITHER = Versions.wrapNmsClassByName("EntityWither");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedEntityWither.ENTITY_WITHER.getConstructor(WrappedWorld.CLAZZ.getClazz());

  WrappedEntityWither(Object reference) {
    super(reference);
  }

  public static WrappedEntityWither construct(@NonNull WrappedWorldServer server)
      throws PacketHandlingException {
    try {
      return new WrappedEntityWither(WrappedEntityWither.CONSTRUCTOR.invoke(server.getWrapped()));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create EntityEnderDragon", e);
    }
  }
}
