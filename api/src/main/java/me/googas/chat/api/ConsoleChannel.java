package me.googas.chat.api;

import java.util.Locale;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.api.scoreboard.ChannelScoreboard;
import me.googas.chat.api.scoreboard.EmptyScoreboard;
import me.googas.chat.wrappers.WrappedSoundCategory;
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
  public void send(@NonNull BaseComponent... components) {
    BukkitUtils.send(Bukkit.getConsoleSender(), components);
  }

  @Override
  public void send(@NonNull String text) {
    Bukkit.getConsoleSender().sendMessage(text);
  }

  @Override
  public void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    if (title != null) this.send(title);
    if (subtitle != null) this.send(subtitle);
  }

  @Override
  public void setRawTabList(String header, String bottom) {
    // Empty
  }

  @Override
  public void playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    // Empty
  }

  @Override
  public void playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    // Empty
  }

  @Override
  public Optional<Locale> getLocale() {
    return Optional.of(ResourceManager.getBase());
  }

  @Override
  public void giveBossBar(@NonNull String text, float progress) {
    // Empty
  }

  @Override
  public @NonNull Optional<? extends AdaptedBossBar> getBossBar() {
    return Optional.empty();
  }

  @Override
  public @NonNull ChannelScoreboard getScoreboard() {
    return new EmptyScoreboard();
  }

  @Override
  public void playSound(
      @NonNull Sound sound, @NonNull WrappedSoundCategory category, float volume, float pitch) {
    // Empty
  }

  @Override
  public void playSound(@NonNull Sound sound, float volume, float pitch) {
    // Empty
  }
}
