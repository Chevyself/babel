package com.github.chevyself.babel.api.channels;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.adapters.bossbar.AdaptedBossBarTuple;
import com.github.chevyself.babel.adapters.bossbar.EmptyAdaptedBossBar;
import com.github.chevyself.babel.api.scoreboard.EmptyScoreboard;
import com.github.chevyself.babel.api.scoreboard.ForwardingScoreboard;
import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
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
  default @NonNull AdaptedBossBar getBossBar() {
    return this.getForward().map(Channel::getBossBar).orElseGet(EmptyAdaptedBossBar::new);
  }

  @Override
  @NonNull
  default ForwardingScoreboard getScoreboard() {
    return () ->
        Collections.singletonList(
            this.getForward().map(Channel::getScoreboard).orElseGet(EmptyScoreboard::new));
  }

  @Override
  default boolean hasBossBar() {
    return this.getForward().map(Channel::hasBossBar).orElse(false);
  }

  @Override
  default boolean hasTabView() {
    return this.getForward().map(Channel::hasTabView).orElse(false);
  }

  @Override
  default boolean hasScoreboard() {
    return this.getForward().map(Channel::hasScoreboard).orElse(false);
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
    default @NonNull AdaptedBossBarTuple getBossBar() {
      return new AdaptedBossBarTuple(
          this.getChannels().stream().map(Channel::getBossBar).collect(Collectors.toList()));
    }

    @Override
    default boolean hasBossBar() {
      return this.getChannels().stream().allMatch(Channel::hasBossBar);
    }

    @Override
    default boolean hasTabView() {
      return this.getChannels().stream().allMatch(Channel::hasTabView);
    }

    @Override
    default boolean hasScoreboard() {
      return this.getChannels().stream().allMatch(Channel::hasScoreboard);
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
