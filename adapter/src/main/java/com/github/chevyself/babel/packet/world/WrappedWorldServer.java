package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

/** Wrapper for the nms WorldServer class. */
public class WrappedWorldServer extends WrappedWorld {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      Versions.wrapNmsClassByName("server.level", "WorldServer");

  /**
   * Create a new WrappedWorldServer.
   *
   * @param reference the object that must be a WorldServer
   */
  public WrappedWorldServer(Object reference) {
    super(reference);
  }
}
