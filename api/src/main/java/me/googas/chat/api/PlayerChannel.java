package me.googas.chat.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.adapters.BossBarAdapter;
import me.googas.chat.adapters.PlayerTabListAdapter;
import me.googas.chat.adapters.PlayerTitleAdapter;
import me.googas.chat.api.scoreboard.PlayerScoreboard;
import me.googas.chat.api.tab.EmptyTabView;
import me.googas.chat.api.tab.PlayerTabView;
import me.googas.chat.api.tab.TabSize;
import me.googas.chat.api.tab.TabView;
import me.googas.chat.api.util.Players;
import me.googas.chat.api.util.Versions;
import me.googas.chat.debug.Debugger;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.sound.WrappedSoundCategory;
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
  default Optional<? extends AdaptedBossBar> getBossBar() {
    return bossBarAdapter.getBossBar(this.getUniqueId());
  }

  @Override
  default void giveBossBar(@NonNull String text, float progress) {
    this.getPlayer()
        .ifPresent(
            player -> {
              Optional<? extends AdaptedBossBar> optional = this.getBossBar();
              if (optional.isPresent()) {
                Debugger.getInstance()
                    .handle(Level.WARNING, "PlayerChannel#giveBossBar without #getBossBar check");
                AdaptedBossBar bossBar = optional.get();
                bossBar.setTitle(text);
                bossBar.setProgress(progress);
              } else {
                bossBarAdapter.create(player, text, progress);
              }
            });
  }

  @Override
  default @NonNull TabView giveTabView() {
    return this.getPlayer()
        .map(
            player -> {
              Optional<? extends TabView> optional = this.getTabView();
              if (optional.isPresent()) {
                Debugger.getInstance()
                    .handle(Level.WARNING, "PlayerChannel#giveTabView without #getTabView check");
                return optional.get();
              } else {
                try {
                  PlayerTabView view = new PlayerTabView(player.getUniqueId(), TabSize.FOUR);
                  view.initialize();
                  views.add(view);
                  return view;
                } catch (PacketHandlingException
                    | InvocationTargetException
                    | InstantiationException
                    | IllegalAccessException e) {
                  Debugger.getInstance()
                      .handle(Level.SEVERE, "Could not initialize tab view for player " + player);
                  return null;
                }
              }
            })
        .orElseGet(EmptyTabView::new);
  }

  @Override
  default @NonNull Optional<? extends TabView> getTabView() {
    return views.stream().filter(view -> view.getUniqueId().equals(this.getUniqueId())).findFirst();
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
