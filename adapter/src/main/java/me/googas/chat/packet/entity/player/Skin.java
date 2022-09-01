package me.googas.chat.packet.entity.player;

import java.lang.reflect.InvocationTargetException;
import lombok.Data;
import lombok.NonNull;
import me.googas.chat.packet.properties.WrappedProperty;

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
