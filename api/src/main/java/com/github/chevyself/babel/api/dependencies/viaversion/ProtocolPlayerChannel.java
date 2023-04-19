package com.github.chevyself.babel.api.dependencies.viaversion;

import com.github.chevyself.babel.api.channels.PlayerChannel;
import com.github.chevyself.babel.util.Versions;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a {@link PlayerChannel} which methods may change due to being in a different protocol
 * version.
 */
public class ProtocolPlayerChannel implements PlayerChannel {

  @NonNull private final UUID uniqueId;
  @NonNull @Getter @Setter private Versions.Player version;

  /**
   * Start the channel.
   *
   * @param uuid the unique id of the player
   */
  protected ProtocolPlayerChannel(@NonNull UUID uuid) {
    this.uniqueId = uuid;
  }

  @Override
  public @NonNull UUID getUniqueId() {
    return uniqueId;
  }

  @Override
  public void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    if (this.version.getBukkit() < 8) {
      if (title != null) this.send(title);
      if (subtitle != null) this.send(subtitle);
    } else {
      PlayerChannel.super.sendRawTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
  }

  @Override
  public void setRawTabList(String header, String bottom) {
    if (this.version.getBukkit() >= 8) {
      PlayerChannel.super.setRawTabList(header, bottom);
    }
  }
}
