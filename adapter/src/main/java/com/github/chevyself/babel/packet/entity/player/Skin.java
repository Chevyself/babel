package com.github.chevyself.babel.packet.entity.player;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.authlib.properties.WrappedProperty;
import lombok.Data;
import lombok.NonNull;

/** Represents the skin of a player. */
@Data
public class Skin {

  @NonNull private final String skin;
  @NonNull private final String signature;

  /**
   * Get the skin as a WrappedProperty.
   *
   * @return the skin as a WrappedProperty
   * @throws PacketHandlingException if an error occurs while constructing the WrappedProperty
   */
  @NonNull
  public WrappedProperty asProperty() throws PacketHandlingException {
    return WrappedProperty.construct("textures", this.skin, this.signature);
  }
}
