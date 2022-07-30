package me.googas.chat.api.softdependencies.viaversion;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.chat.api.Channel;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.util.Versions;

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
  public @NonNull ProtocolPlayerChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    if (this.version.getBukkit() < 8) {
      if (title != null) this.send(title);
      if (title != null) this.send(subtitle);
      return this;
    } else {
      return (ProtocolPlayerChannel)
          PlayerChannel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
  }

  @Override
  public @NonNull Channel setTabList(String header, String bottom) {
    if (this.version.getBukkit() >= 8) {
      return PlayerChannel.super.setTabList(header, bottom);
    }
    return this;
  }
}
