package me.googas.chat.api;

import java.util.Locale;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.LocalizedReference;
import me.googas.chat.sound.WrappedSoundCategory;
import me.googas.commands.bukkit.utils.BukkitUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;

/** A channel that is used to send data to the console. */
public final class ConsoleChannel implements Channel {

  @NonNull @Getter private static final ConsoleChannel instance = new ConsoleChannel();

  private ConsoleChannel() {}

  @Override
  public @NonNull ConsoleChannel send(@NonNull BaseComponent... components) {
    BukkitUtils.send(Bukkit.getConsoleSender(), components);
    return this;
  }

  @Override
  public @NonNull ConsoleChannel send(@NonNull String text) {
    Bukkit.getConsoleSender().sendMessage(text);
    return this;
  }

  @Override
  public @NonNull ConsoleChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    if (title != null) this.send(title);
    if (subtitle != null) this.send(subtitle);
    return this;
  }

  @Override
  public @NonNull ConsoleChannel setTabList(String header, String bottom) {
    return this;
  }

  @Override
  public @NonNull ConsoleChannel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    return this;
  }

  @Override
  public @NonNull ConsoleChannel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    return this;
  }

  @Override
  public @NonNull ConsoleChannel send(@NonNull Line line) {
    return (ConsoleChannel) Channel.super.send(line);
  }

  @Override
  public @NonNull ConsoleChannel send(@NonNull LocalizedReference reference) {
    return (ConsoleChannel) Channel.super.send(reference);
  }

  @Override
  public Optional<Locale> getLocale() {
    return Optional.of(Locale.ENGLISH);
  }
}
