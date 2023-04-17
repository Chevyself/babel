package com.github.chevyself.babel.api.channels;

import chevyself.github.commands.bukkit.utils.BukkitUtils;
import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.adapters.BossBarAdapter;
import com.github.chevyself.babel.adapters.PlayerTabListAdapter;
import com.github.chevyself.babel.adapters.PlayerTitleAdapter;
import com.github.chevyself.babel.adapters.bossbar.EmptyAdaptedBossBar;
import com.github.chevyself.babel.api.lang.Language;
import com.github.chevyself.babel.api.scoreboard.PlayerScoreboard;
import com.github.chevyself.babel.api.tab.EmptyTabView;
import com.github.chevyself.babel.api.tab.PlayerTabView;
import com.github.chevyself.babel.api.tab.TabSize;
import com.github.chevyself.babel.api.tab.TabView;
import com.github.chevyself.babel.api.util.Players;
import com.github.chevyself.babel.api.util.Versions;
import com.github.chevyself.babel.debug.Debugger;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;
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
  @NonNull BossBarAdapter bossBarAdapter = Players.getBossBarAdapter();

  @NonNull Set<PlayerScoreboard> scoreboards = new HashSet<>();

  @NonNull Set<PlayerTabView> views = new HashSet<>();

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
  default void send(@NonNull BaseComponent... components) {
    this.getPlayer().ifPresent(player -> BukkitUtils.send(player, components));
  }

  @Override
  default void send(@NonNull String text) {
    this.getPlayer().ifPresent(player -> player.sendMessage(text));
  }

  @Override
  default void sendRawTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    this.getPlayer()
        .ifPresent(
            player ->
                PlayerChannel.titleAdapter.sendTitle(
                    player, title, subtitle, fadeIn, stay, fadeOut));
  }

  @Override
  default void setRawTabList(String header, String footer) {
    this.getPlayer()
        .ifPresent(player -> PlayerChannel.tabListAdapter.setTabList(player, header, footer));
  }

  @Override
  default void playSound(
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
  }

  @Override
  default void playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    this.getPlayer().ifPresent(player -> player.playSound(location, sound, volume, pitch));
  }

  @Override
  default Optional<Locale> getLocale() {
    return this.getPlayer().map(Language::getLocale);
  }

  @Override
  @NonNull
  default PlayerScoreboard getScoreboard() {
    return scoreboards.stream()
        .filter(scoreboard -> scoreboard.getOwner().equals(this.getUniqueId()))
        .findFirst()
        .orElseGet(
            () -> {
              PlayerScoreboard scoreboard =
                  PlayerScoreboard.create(this.getUniqueId(), new ArrayList<>());
              scoreboards.add(scoreboard);
              return scoreboard;
            });
  }

  @Override
  @NonNull
  default AdaptedBossBar getBossBar() {
    Optional<Player> optional = this.getPlayer();
    if (optional.isPresent()) {
      Player player = optional.get();
      Optional<? extends AdaptedBossBar> bossBar = bossBarAdapter.getBossBar(player.getUniqueId());
      if (bossBar.isPresent()) {
        return bossBar.get();
      } else {
        return bossBarAdapter.create(player);
      }
    } else {
      return new EmptyAdaptedBossBar(this.getUniqueId());
    }
  }

  @Override
  default @NonNull TabView getTabView() {
    Optional<PlayerTabView> tabView =
        views.stream().filter(view -> view.getUniqueId().equals(this.getUniqueId())).findFirst();
    if (tabView.isPresent()) {
      return tabView.get();
    } else {
      return this.getPlayer()
          .map(
              player -> {
                try {
                  PlayerTabView view = new PlayerTabView(player.getUniqueId(), TabSize.FOUR);
                  view.initialize();
                  views.add(view);
                  return view;
                } catch (PacketHandlingException e) {
                  Debugger.getInstance()
                      .handle(Level.SEVERE, "Could not initialize tab view for player " + player);
                  return new EmptyTabView();
                }
              })
          .orElseGet(EmptyTabView::new);
    }
  }


  @Override
  default boolean hasBossBar() {
    return bossBarAdapter.getBossBar(this.getUniqueId()).isPresent();
  }

  @Override
  default boolean hasTabView() {
    return views.stream().anyMatch(view -> view.getUniqueId().equals(this.getUniqueId()));
  }

  @Override
  default boolean hasScoreboard() {
    return scoreboards.stream()
        .anyMatch(scoreboard -> scoreboard.getOwner().equals(this.getUniqueId()));
  }

  @Override
  default void playSound(
      @NonNull Sound sound, @NonNull WrappedSoundCategory category, float volume, float pitch) {
    this.getPlayer()
        .ifPresent(player -> this.playSound(player.getLocation(), sound, category, volume, pitch));
  }

  @Override
  default void playSound(@NonNull Sound sound, float volume, float pitch) {
    this.getPlayer()
        .ifPresent(player -> this.playSound(player.getLocation(), sound, volume, pitch));
  }
}