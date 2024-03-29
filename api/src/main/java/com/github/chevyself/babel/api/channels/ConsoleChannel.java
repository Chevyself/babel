package com.github.chevyself.babel.api.channels;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.adapters.bossbar.EmptyAdaptedBossBar;
import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.scoreboard.ChannelScoreboard;
import com.github.chevyself.babel.api.scoreboard.EmptyScoreboard;
import com.github.chevyself.babel.api.tab.EmptyTabView;
import com.github.chevyself.babel.api.tab.TabView;
import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
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
  public @NonNull AdaptedBossBar getBossBar() {
    return new EmptyAdaptedBossBar(UUID.randomUUID());
  }

  @Override
  public @NonNull TabView getTabView() {
    return new EmptyTabView();
  }

  @Override
  public @NonNull ChannelScoreboard getScoreboard() {
    return new EmptyScoreboard();
  }

  @Override
  public boolean hasBossBar() {
    return false;
  }

  @Override
  public boolean hasTabView() {
    return false;
  }

  @Override
  public boolean hasScoreboard() {
    return false;
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
