package me.googas.chat.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.LocalizedReference;
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
  @NonNull
  default ForwardingChannel sendTitle(
      Line title, Line subtitle, int fadeIn, int stay, int fadeOut) {
    return (ForwardingChannel) Channel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  @NonNull
  default ForwardingChannel sendTitle(
      LocalizedReference title, LocalizedReference subtitle, int fadeIn, int stay, int fadeOut) {
    return (ForwardingChannel) Channel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull Line line) {
    return (ForwardingChannel) Channel.super.send(line);
  }

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull LocalizedReference reference) {
    return (ForwardingChannel) Channel.super.send(reference);
  }

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull BaseComponent... components) {
    this.getForward().ifPresent(channel -> channel.send(components));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull String text) {
    this.getForward().ifPresent(channel -> channel.send(text));
    return this;
  }

  @Override
  default Optional<Locale> getLocale() {
    return this.getForward().flatMap(Channel::getLocale);
  }

  @Override
  @NonNull
  default ForwardingChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    this.getForward()
        .ifPresent(channel -> channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel setTabList(String header, String bottom) {
    this.getForward().ifPresent(channel -> channel.setTabList(header, bottom));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    this.getForward()
        .ifPresent(channel -> channel.playSound(location, sound, category, volume, pitch));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    this.getForward().ifPresent(channel -> channel.playSound(location, sound, volume, pitch));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel setTabList(Line header, Line bottom) {
    return (ForwardingChannel) Channel.super.setTabList(header, bottom);
  }

  @Override
  @NonNull
  default ForwardingChannel setTabList(LocalizedReference header, LocalizedReference bottom) {
    return (ForwardingChannel) Channel.super.setTabList(header, bottom);
  }

  @Override
  @NonNull
  default ForwardingChannel giveBossBar(@NonNull String text, float progress) {
    this.getForward().ifPresent(channel -> channel.giveBossBar(text, progress));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel giveBossBar(@NonNull Line text, float progress) {
    return (ForwardingChannel) Channel.super.giveBossBar(text, progress);
  }

  @Override
  @NonNull
  default ForwardingChannel giveBossBar(@NonNull LocalizedReference reference, float progress) {
    return (ForwardingChannel) Channel.super.giveBossBar(reference, progress);
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
    @NonNull
    default Multiple setTabList(Line header, Line bottom) {
      return (Multiple) Channel.super.setTabList(header, bottom);
    }

    @Override
    @NonNull
    default Multiple setTabList(LocalizedReference header, LocalizedReference bottom) {
      return (Multiple) Channel.super.setTabList(header, bottom);
    }

    @Override
    @NonNull
    default Multiple giveBossBar(@NonNull String text, float progress) {
      this.getChannels().forEach(channel -> channel.giveBossBar(text, progress));
      return this;
    }

    @Override
    @NonNull
    default Multiple giveBossBar(@NonNull Line text, float progress) {
      return (Multiple) Channel.super.giveBossBar(text, progress);
    }

    @Override
    @NonNull
    default Multiple giveBossBar(@NonNull LocalizedReference reference, float progress) {
      return (Multiple) Channel.super.giveBossBar(reference, progress);
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
    @NonNull
    default Multiple send(@NonNull BaseComponent... components) {
      this.getChannels().forEach(channel -> channel.send(components));
      return this;
    }

    @Override
    @NonNull
    default Multiple sendTitle(Line title, Line subtitle, int fadeIn, int stay, int fadeOut) {
      return (Multiple) Channel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    @NonNull
    default Multiple sendTitle(
        LocalizedReference title, LocalizedReference subtitle, int fadeIn, int stay, int fadeOut) {
      return (Multiple) Channel.super.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    @NonNull
    default Multiple send(@NonNull Line line) {
      return (Multiple) Channel.super.send(line);
    }

    @Override
    @NonNull
    default Multiple send(@NonNull LocalizedReference reference) {
      return (Multiple) Channel.super.send(reference);
    }

    @Override
    @NonNull
    default Multiple send(@NonNull String text) {
      this.getChannels().forEach(channel -> channel.send(text));
      return this;
    }

    @Override
    default Optional<Locale> getLocale() {
      return Optional.empty();
    }

    @Override
    @NonNull
    default Multiple playSound(
        @NonNull Location location,
        @NonNull Sound sound,
        @NonNull WrappedSoundCategory category,
        float volume,
        float pitch) {
      this.getChannels()
          .forEach(channel -> channel.playSound(location, sound, category, volume, pitch));
      return this;
    }

    @Override
    @NonNull
    default Multiple playSound(
        @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
      this.getChannels().forEach(channel -> channel.playSound(location, sound, volume, pitch));
      return this;
    }

    @Override
    @NonNull
    default Multiple sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      this.getChannels()
          .forEach(channel -> channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
      return this;
    }

    @Override
    @NonNull
    default Multiple setTabList(String header, String bottom) {
      this.getChannels().forEach(channel -> channel.setTabList(header, bottom));
      return this;
    }
  }
}
