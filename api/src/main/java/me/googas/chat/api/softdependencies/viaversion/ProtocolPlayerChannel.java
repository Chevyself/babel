package me.googas.chat.api.softdependencies.viaversion;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.LocalizedReference;
import me.googas.chat.api.util.Versions;
import me.googas.chat.wrappers.WrappedSoundCategory;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.Sound;

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
      if (subtitle != null) this.send(subtitle);
      return this;
    } else {
      return (ProtocolPlayerChannel)
          PlayerChannel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
  }

  @Override
  public @NonNull ProtocolPlayerChannel sendTitle(
      Line title, Line subtitle, int fadeIn, int stay, int fadeOut) {
    return (ProtocolPlayerChannel)
        PlayerChannel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  public @NonNull ProtocolPlayerChannel sendTitle(
      LocalizedReference title, LocalizedReference subtitle, int fadeIn, int stay, int fadeOut) {
    return (ProtocolPlayerChannel)
        PlayerChannel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  public @NonNull ProtocolPlayerChannel setTabList(String header, String bottom) {
    if (this.version.getBukkit() >= 8) {
      return (ProtocolPlayerChannel) PlayerChannel.super.setTabList(header, bottom);
    }
    return this;
  }

  @Override
  public @NonNull ProtocolPlayerChannel send(@NonNull Line line) {
    return (ProtocolPlayerChannel) PlayerChannel.super.send(line);
  }

  @Override
  public @NonNull ProtocolPlayerChannel send(@NonNull LocalizedReference reference) {
    return (ProtocolPlayerChannel) PlayerChannel.super.send(reference);
  }

  @Override
  public @NonNull ProtocolPlayerChannel send(@NonNull BaseComponent... components) {
    return (ProtocolPlayerChannel) PlayerChannel.super.send(components);
  }

  @Override
  public @NonNull ProtocolPlayerChannel send(@NonNull String text) {
    return (ProtocolPlayerChannel) PlayerChannel.super.send(text);
  }

  @Override
  public @NonNull ProtocolPlayerChannel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    return (ProtocolPlayerChannel)
        PlayerChannel.super.playSound(location, sound, category, volume, pitch);
  }

  @Override
  public @NonNull ProtocolPlayerChannel setTabList(Line header, Line bottom) {
    return (ProtocolPlayerChannel) PlayerChannel.super.setTabList(header, bottom);
  }

  @Override
  public @NonNull ProtocolPlayerChannel setTabList(
      LocalizedReference header, LocalizedReference bottom) {
    return (ProtocolPlayerChannel) PlayerChannel.super.setTabList(header, bottom);
  }

  @Override
  public @NonNull ProtocolPlayerChannel giveBossBar(@NonNull Line text, float progress) {
    return (ProtocolPlayerChannel) PlayerChannel.super.giveBossBar(text, progress);
  }

  @Override
  public @NonNull ProtocolPlayerChannel giveBossBar(
      @NonNull LocalizedReference reference, float progress) {
    return (ProtocolPlayerChannel) PlayerChannel.super.giveBossBar(reference, progress);
  }

  @Override
  public @NonNull Optional<? extends AdaptedBossBar> getBossBar() {
    return PlayerChannel.super.getBossBar();
  }

  @Override
  public @NonNull ProtocolPlayerChannel giveBossBar(@NonNull String text, float progress) {
    return (ProtocolPlayerChannel) PlayerChannel.super.giveBossBar(text, progress);
  }

  @Override
  public @NonNull ProtocolPlayerChannel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    return (ProtocolPlayerChannel) PlayerChannel.super.playSound(location, sound, volume, pitch);
  }
}
