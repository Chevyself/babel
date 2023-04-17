package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.packet.properties.WrappedProperty;
import java.lang.reflect.InvocationTargetException;
import lombok.Data;
import lombok.NonNull;

@Data
public class Skin {

  @NonNull private final String skin;
  @NonNull private final String signature;

  @NonNull
  public WrappedProperty asProperty()
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return WrappedProperty.construct("textures", this.skin, this.signature);
  }
}
