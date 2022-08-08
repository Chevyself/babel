package me.googas.chat.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.api.scoreboard.EmptyScoreboard;
import me.googas.chat.api.scoreboard.ForwardingScoreboard;
import me.googas.chat.wrappers.WrappedSoundCategory;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.Sound;

/** A forwarding channel is a channel which wraps another channel to send data. */
public interface ForwardingChannel extends Channel {

  /**
   * Get the channel that is being forwarded.
   *
   * @return a {@link Optional} holding the nullable channel
   */
  @NonNull
  Optional<Channel> getForward();

  @Override
  default void playSound(
      @NonNull Sound sound, @NonNull WrappedSoundCategory category, float volume, float pitch) {
    this.getForward().ifPresent(channel -> channel.playSound(sound, category, volume, pitch));
  }

  @Override
  default void playSound(@NonNull Sound sound, float volume, float pitch) {
    this.getForward().ifPresent(channel -> channel.playSound(sound, volume, pitch));
  }

  @Override
  default void send(@NonNull BaseComponent... components) {
    this.getForward().ifPresent(channel -> channel.send(components));
  }

  @Override
  default void send(@NonNull String text) {
    this.getForward().ifPresent(channel -> channel.send(text));
  }

  @Override
  default Optional<Locale> getLocale() {
    return this.getForward().flatMap(Channel::getLocale);
  }

  @Override
  default void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    this.getForward()
        .ifPresent(channel -> channel.sendRawTitle(title, subtitle, fadeIn, stay, fadeOut));
  }

  @Override
  default void setRawTabList(String header, String bottom) {
    this.getForward().ifPresent(channel -> channel.setRawTabList(header, bottom));
  }

  @Override
  default void playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    this.getForward()
        .ifPresent(channel -> channel.playSound(location, sound, category, volume, pitch));
  }

  @Override
  default void playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    this.getForward().ifPresent(channel -> channel.playSound(location, sound, volume, pitch));
  }

  @Override
  default void giveBossBar(@NonNull String text, float progress) {
    this.getForward().ifPresent(channel -> channel.giveBossBar(text, progress));
  }

  @Override
  @NonNull
  default Optional<? extends AdaptedBossBar> getBossBar() {
    return this.getForward().flatMap(Channel::getBossBar);
  }

  @Override
  @NonNull
  default ForwardingScoreboard getScoreboard() {
    return () ->
        Collections.singletonList(
            this.getForward().map(Channel::getScoreboard).orElseGet(EmptyScoreboard::new));
  }

  /** This type of forwarding channel wraps more than one channel. */
  interface Multiple extends Channel {

    @Override
    @NonNull
    default ForwardingScoreboard getScoreboard() {
      return () ->
          this.getChannels().stream().map(Channel::getScoreboard).collect(Collectors.toList());
    }

    @Override
    default void giveBossBar(@NonNull String text, float progress) {
      this.getChannels().forEach(channel -> channel.giveBossBar(text, progress));
    }

    @Override
    @NonNull
    default Optional<? extends AdaptedBossBar> getBossBar() {
      return Optional.empty();
    }

    /**
     * Get all the wrapped channels.
     *
     * @return this same instance
     */
    @NonNull
    Collection<? extends Channel> getChannels();

    @Override
    default void send(@NonNull BaseComponent... components) {
      this.getChannels().forEach(channel -> channel.send(components));
    }

    @Override
    default void playSound(
        @NonNull Sound sound, @NonNull WrappedSoundCategory category, float volume, float pitch) {
      this.getChannels().forEach(channel -> channel.playSound(sound, category, volume, pitch));
    }

    @Override
    default void playSound(@NonNull Sound sound, float volume, float pitch) {
      this.getChannels().forEach(channel -> channel.playSound(sound, volume, pitch));
    }

    @Override
    default void send(@NonNull String text) {
      this.getChannels().forEach(channel -> channel.send(text));
    }

    @Override
    default Optional<Locale> getLocale() {
      return Optional.empty();
    }

    @Override
    default void playSound(
        @NonNull Location location,
        @NonNull Sound sound,
        @NonNull WrappedSoundCategory category,
        float volume,
        float pitch) {
      this.getChannels()
          .forEach(channel -> channel.playSound(location, sound, category, volume, pitch));
    }

    @Override
    default void playSound(
        @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
      this.getChannels().forEach(channel -> channel.playSound(location, sound, volume, pitch));
    }

    @Override
    default void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      this.getChannels()
          .forEach(channel -> channel.sendRawTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    default void setRawTabList(String header, String bottom) {
      this.getChannels().forEach(channel -> channel.setRawTabList(header, bottom));
    }
  }
}
