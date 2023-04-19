package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

public class WrappedWorldServer extends WrappedWorld {

  @NonNull public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("WorldServer");

  WrappedWorldServer(Object reference) {
    super(reference);
  }
}
