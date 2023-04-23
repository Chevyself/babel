package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;

/** This class is used to wrap the RemoteChatSession$a nms class. */
public class WrappedRemoteChatSessionData extends ReflectWrapper {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      Versions.wrapNmsClassByName("network.chat", "RemoteChatSession$a");

  @Override
  public Class<?> getReflectClass() {
    return WrappedRemoteChatSessionData.CLAZZ.getClazz();
  }
}
