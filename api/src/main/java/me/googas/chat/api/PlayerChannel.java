package me.googas.chat.api;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.chat.adapters.PlayerTabListAdapter;
import me.googas.chat.adapters.PlayerTitleAdapter;
import me.googas.chat.api.util.Players;
import me.googas.chat.api.util.Versions;
import me.googas.chat.sound.WrappedSoundCategory;
import me.googas.commands.bukkit.utils.BukkitUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/** A channel that is used to send data to a player. */
public interface PlayerChannel extends Channel {

  @NonNull PlayerTitleAdapter titleAdapter = Players.getTitleAdapter();
  @NonNull PlayerTabListAdapter tabListAdapter = Players.getTabListAdapter();

  /**
   * Get the unique id of the player.
   *
   * @return the unique id
   */
  @NonNull
  UUID getUniqueId();

  /**
   * Get the player.
   *
   * @return a {@link Optional} holding the nullable player
   */
  @NonNull
  default Optional<Player> getPlayer() {
    return Optional.ofNullable(this.getOffline().getPlayer());
  }

  /**
   * Get the offline players.
   *
   * @return the offline player
   */
  @NonNull
  default OfflinePlayer getOffline() {
    return Bukkit.getOfflinePlayer(this.getUniqueId());
  }

  @Override
  @NonNull
  default PlayerChannel send(@NonNull BaseComponent... components) {
    this.getPlayer().ifPresent(player -> BukkitUtils.send(player, components));
    return this;
  }

  @Override
  @NonNull
  default PlayerChannel send(@NonNull String text) {
    this.getPlayer().ifPresent(player -> player.sendMessage(text));
    return this;
  }

  @Override
  default @NonNull PlayerChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    this.getPlayer()
        .ifPresent(
            player ->
                PlayerChannel.titleAdapter.sendTitle(
                    player, title, subtitle, fadeIn, stay, fadeOut));
    return this;
  }

  @Override
  default @NonNull Channel setTabList(String header, String footer) {
    this.getPlayer()
        .ifPresent(player -> PlayerChannel.tabListAdapter.setTabList(player, header, footer));
    return this;
  }

  @Override
  default @NonNull Channel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    this.getPlayer()
        .ifPresent(
            player -> {
              if (Versions.BUKKIT < 11 || !category.get().isPresent()) {
                player.playSound(location, sound, volume, pitch);
              } else {
                player.playSound(location, sound, category.get().get(), volume, pitch);
              }
            });
    return this;
  }

  @Override
  default @NonNull Channel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    this.getPlayer().ifPresent(player -> player.playSound(location, sound, volume, pitch));
    return this;
  }

  @Override
  default Optional<Locale> getLocale() {
    return this.getPlayer().map(Language::getLocale);
  }
}
