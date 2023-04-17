package com.github.chevyself.babel.api.channels;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.api.dependencies.viaversion.ViaVersionSoft;
import com.github.chevyself.babel.api.scoreboard.ChannelScoreboard;
import com.github.chevyself.babel.api.tab.TabView;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A channel to send different types of data, such as text messages, titles, action bars, sounds, or
 * boss bars. It can also edit the player's tab list or scoreboard.
 */
public interface Channel {

  @NonNull List<PlayerChannel> players = new ArrayList<>();

  /**
   * Get the channel of a {@link CommandSender}.
   *
   * @param sender the sender to get the channel from
   * @return the channel of the sender
   * @throws NullPointerException if the sender is null
   */
  @NonNull
  static Channel of(@NonNull CommandSender sender) {
    if (sender instanceof Player) {
      return Channel.of((OfflinePlayer) sender);
    } else {
      return ConsoleChannel.getInstance();
    }
  }

  /**
   * Get the channel of a {@link OfflinePlayer}.
   *
   * @param player the player to get the channel from
   * @return the channel
   * @throws NullPointerException if the player is null
   */
  static PlayerChannel of(@NonNull OfflinePlayer player) {
    return Channel.of(player.getUniqueId());
  }

  /**
   * Get the channel of a {@link Player}.
   *
   * @param player the player to get the channel from
   * @return the channel
   * @throws NullPointerException if the player is null
   */
  @NonNull
  static PlayerChannel of(@NonNull Player player) {
    return Channel.of(player.getUniqueId());
  }

  /**
   * Get the channel of a {@link Player} based on its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the channel of the player
   * @throws NullPointerException if the unique id is null
   */
  @NonNull
  static PlayerChannel of(@NonNull UUID uniqueId) {
    return new ArrayList<>(Channel.players)
        .stream()
            .filter(channel -> channel.getUniqueId().equals(uniqueId))
            .findFirst()
            .orElseGet(
                () -> {
                  PlayerChannel channel;
                  if (ViaVersionSoft.isEnabled()) {
                    channel = ViaVersionSoft.getProtocolChannel(uniqueId);
                  } else {
                    channel = () -> uniqueId;
                  }
                  Channel.players.add(channel);
                  return channel;
                });
  }

  /**
   * Send base components to the channel.
   *
   * @param components the components to send
   */
  void send(@NonNull BaseComponent... components);

  /**
   * Send a text message to the channel.
   *
   * @param text the text message to send
   */
  void send(@NonNull String text);

  /**
   * Send text to this channel.
   *
   * @param text the text to send
   */
  default void send(@NonNull Text text) {
    this.send(text.build(this));
  }

  /**
   * Send a title to this channel.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @param fadeIn how long until the title appears in ticks
   * @param stay how long until the title stays in ticks
   * @param fadeOut how long until the title fades in ticks
   */
  void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

  /**
   * Send a title to this channel.
   *
   * @param title the title as line
   * @param subtitle the subtitle as line
   * @param fadeIn how long until the title appears in ticks
   * @param stay how long until the title stays in ticks
   * @param fadeOut how long until the title fades in ticks
   */
  default void sendTitle(Text title, Text subtitle, int fadeIn, int stay, int fadeOut) {
    this.sendRawTitle(
        title == null ? null : title.asString(this),
        subtitle == null ? null : subtitle.asString(this),
        fadeIn,
        stay,
        fadeOut);
  }

  /**
   * Set the header and bottom of the tab-list.
   *
   * @param header the header text to set
   * @param bottom the bottom text to set
   */
  void setRawTabList(String header, String bottom);

  /**
   * Set the header and bottom of the tab-list.
   *
   * @param header the header text to set
   * @param bottom the bottom text to set
   */
  default void setTabList(Text header, Text bottom) {
    this.setRawTabList(
        header == null ? null : header.asString(this),
        bottom == null ? null : bottom.asString(this));
  }

  /**
   * Play sound to a channel.
   *
   * @param sound the sound to play
   * @param category the category in which the sound will play
   * @param volume the volume of the sound
   * @param pitch the pitch of the sound
   */
  void playSound(
      @NonNull Sound sound, @NonNull WrappedSoundCategory category, float volume, float pitch);

  /**
   * Play sound to a channel.
   *
   * @param sound the sound to play
   * @param volume the volume of the sound
   * @param pitch the pitch of the sound
   */
  void playSound(@NonNull Sound sound, float volume, float pitch);

  /**
   * Play sound to a channel.
   *
   * @param location the location in which the sound will play
   * @param sound the sound to play
   * @param category the category in which the sound will play
   * @param volume the volume of the sound
   * @param pitch the pitch of the sound
   */
  void playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch);

  /**
   * Play sound to a channel.
   *
   * @param location the location in which the sound will play
   * @param sound the sound to play
   * @param volume the volume of the sound
   * @param pitch the pitch of the sound
   */
  void playSound(@NonNull Location location, @NonNull Sound sound, float volume, float pitch);

  /**
   * Get the locale of the channel.
   *
   * @return the locale
   */
  Optional<Locale> getLocale();

  @NonNull
  AdaptedBossBar getBossBar();

  @NonNull
  TabView getTabView();

  @NonNull
  ChannelScoreboard getScoreboard();

  /**
   * Checks whether this channel has a boss bar
   *
   * @return true if the channel has a boss bar
   */
  boolean hasBossBar();

  /**
   * Checks whether this channel has a tab view
   *
   * @return true if the channel has a tab view
   */
  boolean hasTabView();

  /**
   * Checks whether this channel has a scoreboard
   *
   * @return true if the channel has a scoreboard
   */
  boolean hasScoreboard();
}
