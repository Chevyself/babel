package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.world.WrappedWorld;
import com.github.chevyself.babel.packet.world.WrappedWorldServer;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wraps the EntityWither nms class. */
public class WrappedEntityWither extends WrappedEntityLiving {

  @NonNull
  private static final WrappedClass<?> ENTITY_WITHER = Versions.wrapNmsClassByName("EntityWither");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedEntityWither.ENTITY_WITHER.getConstructor(WrappedWorld.CLAZZ.getClazz());

  WrappedEntityWither(Object reference) {
    super(reference);
  }

  /**
   * Create a new wither.
   *
   * @param server the world to create the wither in
   * @return the wither
   * @throws PacketHandlingException if the wither could not be created
   */
  public static WrappedEntityWither construct(@NonNull WrappedWorldServer server)
      throws PacketHandlingException {
    try {
      return new WrappedEntityWither(WrappedEntityWither.CONSTRUCTOR.invoke(server.getWrapped()));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create EntityEnderDragon", e);
    }
  }
}
