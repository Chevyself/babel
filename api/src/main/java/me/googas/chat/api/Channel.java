package me.googas.chat.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.scoreboard.ChannelScoreboard;
import me.googas.chat.api.softdependencies.viaversion.ViaVersionSoft;
import me.googas.chat.wrappers.WrappedSoundCategory;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** A channel to send different type of data. */
public interface Channel {

  @NonNull List<PlayerChannel> players = new ArrayList<>();

  /**
   * Get the channel of a {@link CommandSender}.
   *
   * @param sender the sender to get the channel from
   * @return the channel of the sender
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
   */
  static PlayerChannel of(@NonNull OfflinePlayer player) {
    return Channel.of(player.getUniqueId());
  }

  /**
   * Get the channel of a {@link Player}.
   *
   * @param player the player to get the channel from
   * @return the channel
   */
  static PlayerChannel of(@NonNull Player player) {
    return Channel.of(player.getUniqueId());
  }

  /**
   * Get the channel of a {@link Player} based on its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the channel of the player
   */
  static PlayerChannel of(@NonNull UUID uniqueId) {
    return new ArrayList<>(Channel.players)
        .stream()
            .filter(channel -> channel.getUniqueId().equals(uniqueId))
            .findFirst()
            .orElseGet(
                () -> {
                  PlayerChannel channel;
                  if (ViaVersionSoft.isEnabled()) {
                    return ViaVersionSoft.getProtocolChannel(uniqueId);
                  } else {
                    channel = () -> uniqueId;
                    Channel.players.add(channel);
                    return channel;
                  }
                });
  }

  /**
   * Send base components to the channel.
   *
   * @param components the components to send
   * @return this same instance
   */
  @NonNull
  Channel send(@NonNull BaseComponent... components);

  /**
   * Send a text message to the channel.
   *
   * @param text the text message to send
   * @return this same instance
   */
  @NonNull
  Channel send(@NonNull String text);

  /**
   * Send a line to this channel.
   *
   * @param line the line to send
   * @return this same instance
   */
  @NonNull
  default Channel send(@NonNull Line line) {
    return this.send(line.build(this));
  }

  /**
   * Send a title to this channel.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @param fadeIn how long until the title appears in ticks
   * @param stay how long until the title stays in ticks
   * @param fadeOut how long until the title fades in ticks
   * @return this same instance
   */
  @NonNull
  Channel sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

  /**
   * Send a title to this channel.
   *
   * @param title the title as a line
   * @param subtitle the subtitle as a line
   * @param fadeIn how long until the title appears in ticks
   * @param stay how long until the title stays in ticks
   * @param fadeOut how long until the title fades in ticks
   * @return this same instance
   */
  @NonNull
  default Channel sendTitle(Line title, Line subtitle, int fadeIn, int stay, int fadeOut) {
    return this.sendRawTitle(
        title == null ? null : title.asText(this),
        subtitle == null ? null : subtitle.asText(this),
        fadeIn,
        stay,
        fadeOut);
  }

  /**
   * Set the header and bottom of the tab-list.
   *
   * @param header the header text to set
   * @param bottom the bottom text to set
   * @return this same instance
   */
  @NonNull
  Channel setRawTabList(String header, String bottom);

  /**
   * Set the header and bottom of the tab-list.
   *
   * @param header the header text to set as a line
   * @param bottom the bottom text to set as a line
   * @return this same instance
   */
  @NonNull
  default Channel setTabList(Line header, Line bottom) {
    return this.setRawTabList(
        header == null ? null : header.asText(this), bottom == null ? null : bottom.asText(this));
  }

  /**
   * Play sound to a channel.
   *
   * @param location the location in which the sound will play
   * @param sound the sound to play
   * @param category the category in which the sound will play
   * @param volume the volume of the sound
   * @param pitch the pitch of the sound
   * @return this same instance
   */
  @NonNull
  Channel playSound(
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
   * @return this same instance
   */
  @NonNull
  Channel playSound(@NonNull Location location, @NonNull Sound sound, float volume, float pitch);

  /**
   * Get the locale of the channel.
   *
   * @return the locale
   */
  Optional<Locale> getLocale();

  @NonNull
  Channel giveBossBar(@NonNull String text, float progress);

  @NonNull
  default Channel giveBossBar(@NonNull Line text, float progress) {
    return this.giveBossBar(text.asText(this), progress);
  }

  @NonNull
  Optional<? extends AdaptedBossBar> getBossBar();

  @NonNull
  ChannelScoreboard getScoreboard();
}
