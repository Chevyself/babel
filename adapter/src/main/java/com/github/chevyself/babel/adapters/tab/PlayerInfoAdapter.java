package com.github.chevyself.babel.adapters.tab;

import com.github.chevyself.babel.packet.authlib.WrappedGameProfile;
import com.github.chevyself.babel.packet.chat.WrappedChatComponent;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfo;
import com.github.chevyself.babel.packet.entity.player.WrappedPlayerInfoEntry;
import com.github.chevyself.babel.packet.world.WrappedEnumGameMode;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

/**
 * Adapts the 'PlayerInfo' class to the server version.
 *
 * <p>Such class is used to send fake players to the client and create a custom tab.
 *
 * <p>In versions previous to 1.19.3 the method {@link #asPlayerInfoEntry()} may be used, meanwhile
 * in 1.8+ the method {@link #asPlayerInfoData()} may be used.
 */
public final class PlayerInfoAdapter {

  @NonNull @Getter private final UUID uniqueId;
  @NonNull private final WrappedGameProfile profile;
  private final int ping;
  @NonNull private final WrappedEnumGameMode gameMode;
  @NonNull private final WrappedChatComponent display;

  /**
   * Create the adapter.
   *
   * @param profile the fake player's profile
   * @param ping the fake player's ping
   * @param gameMode the fake player's game mode
   * @param display the fake player's display name
   */
  public PlayerInfoAdapter(
      @NonNull WrappedGameProfile profile,
      int ping,
      @NonNull WrappedEnumGameMode gameMode,
      @NonNull WrappedChatComponent display) {
    this.uniqueId = profile.getId();
    this.profile = profile;
    this.ping = ping;
    this.gameMode = gameMode;
    this.display = display;
  }

  /**
   * Get the player info data.
   *
   * <p>Used for versions 1.8+. Until 1.19.3 the method {@link #asPlayerInfoEntry()} may be used.
   *
   * @return the player info data wrapped
   */
  @NonNull
  public WrappedPlayerInfo asPlayerInfoData() {
    return WrappedPlayerInfo.construct(null, profile, ping, gameMode, display);
  }

  /**
   * Get the player info entry
   *
   * <p>Used for versions greater or equal than 1.19.3. From 1.19.2 and previous versions the
   * method {@link #asPlayerInfoData()} may be used.
   *
   * @return the player info entry wrapped
   */
  @NonNull
  public WrappedPlayerInfoEntry asPlayerInfoEntry() {
    return WrappedPlayerInfoEntry.construct(
        this.uniqueId, profile, true, ping, gameMode, display, null);
  }
}
