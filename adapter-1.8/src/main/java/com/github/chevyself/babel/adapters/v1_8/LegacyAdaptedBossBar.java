package com.github.chevyself.babel.adapters.v1_8;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import com.github.chevyself.babel.packet.craft.WrappedCraftWorld;
import com.github.chevyself.babel.packet.entity.WrappedDataWatcher;
import com.github.chevyself.babel.packet.entity.WrappedEntityWither;
import com.github.chevyself.babel.packet.world.WrappedWorldServer;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.util.ReflectUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/** Creates a boss bar for 1.8 clients. */
public class LegacyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;
  private String title;
  private float progress;
  private WrappedEntityWither wither;
  @Getter private boolean destroyed;

  /**
   * Creates a new boss bar for the given player.
   *
   * @param owner the unique id that owns this boss bar
   */
  public LegacyAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
    this.title = "";
    this.progress = 1;
    this.destroyed = false;
  }

  @NonNull
  static Location getWitherLocation(Location location) {
    return location.add(location.getDirection().multiply(23));
  }

  @NonNull
  private LegacyAdaptedBossBar setMetadata(@NonNull Runnable runnable) {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      runnable.run();
      if (this.isDisplayed()) {
        this.callMetadataPacket(bukkit.get());
      }
    } else {
      this.destroy();
    }
    return this;
  }

  @NonNull
  @Override
  public LegacyAdaptedBossBar setTitle(@NonNull String title) {
    return this.setMetadata(() -> this.title = title);
  }

  @NonNull
  @Override
  public LegacyAdaptedBossBar setProgress(float progress) {
    if (progress <= 0) {
      this.destroy();
    } else {
      if (progress > 1) {
        progress = 1;
      }
      float finalProgress = progress;
      this.setMetadata(() -> this.progress = finalProgress);
    }
    return this;
  }

  private void callMetadataPacket(@NonNull Player player) {
    try {
      Packet packet =
          PacketType.Play.ClientBound.ENTITY_METADATA.create(
              new Class[]{int.class, WrappedDataWatcher.CLAZZ.getClazz(), boolean.class},
              wither.getId(), this.getDataWatcher(), true);
      packet.send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Failed to send metadata packet", e);
    }
  }

  /**
   * Get the data watcher for the wither.
   *
   * <p>This method adds the metadata to the wither. It also allows the wither to be invisible.
   *
   * <p>Please note that in order to not display the withers shield, meaning that its health or the
   * percentage of the boss bar is below 50%, the boss bar must be displayed with a percentage above
   * 50% and then the shield will not be displayed.
   *
   * <p>The method 'a' in Data watcher was removed in the next versions but 1.9 added a boss bar
   * API. Making this method obsolete.
   *
   * @return the data watcher
   * @throws PacketHandlingException if the data watcher could not be created
   */
  @SuppressWarnings("deprecation")
  @NonNull
  private Object getDataWatcher() throws PacketHandlingException {
    WrappedDataWatcher dataWatcher = WrappedDataWatcher.construct();
    dataWatcher.a(0, (byte) 0x20);
    dataWatcher.a(2, this.title);
    dataWatcher.a(3, (byte) 1);
    dataWatcher.a(6, progress * wither.getMaxHealth());
    dataWatcher.a(8, (byte) 0);
    dataWatcher.a(10, this.title);
    dataWatcher.a(11, (byte) 1);
    dataWatcher.a(17, 0);
    dataWatcher.a(18, 0);
    dataWatcher.a(19, 0);
    dataWatcher.a(20, 881);
    return ReflectUtil.nonNullWrapped(
        dataWatcher, new PacketHandlingException("DataWatcher was not created successfully"));
  }

  @Override
  public @NonNull LegacyAdaptedBossBar setColor(@NonNull WrappedBarColor color) {
    return this;
  }

  @Override
  public @NonNull LegacyAdaptedBossBar setStyle(@NonNull WrappedBarStyle style) {
    return this;
  }

  @Override
  public boolean isDisplayed() {
    return wither != null && wither.isPresent();
  }

  /**
   * Teleports the wither to the player.
   *
   * <p>If the wither is not in sight of the player, the player will not be able to see it,
   * therefore, this method must be called in a {@link org.bukkit.scheduler.BukkitTask} every tick
   * or every few ticks.
   */
  public void teleport() {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      if (!this.isDisplayed()) {
        return;
      }
      try {
        Player player = bukkit.get();
        Location location = LegacyAdaptedBossBar.getWitherLocation(player.getLocation());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        Packet packet =
            PacketType.Play.ClientBound.ENTITY_TELEPORT.create(
                ReflectUtil.nonNullWrapped(
                    wither, new PacketHandlingException("Wither is no longer reachable")));
        packet.send(player);
      } catch (PacketHandlingException e) {
        Debugger.getInstance().getLogger().severe("Failed to send teleport packet");
      }
    } else {
      this.destroy();
    }
  }

  @Override
  public void destroy() {
    if (!destroyed) {
      destroyed = true;
      if (!this.isDisplayed()) {
        return;
      }
      this.getOwnerBukkit()
          .ifPresent(
              player -> {
                try {
                  if (wither.isPresent()) {
                    Packet packet = PacketType.Play.ClientBound.ENTITY_DESTROY.create();
                    packet.setField(0, new int[] {wither.getId()});
                    packet.send(player);
                  }
                } catch (PacketHandlingException e) {
                  Debugger.getInstance()
                      .getLogger()
                      .log(Level.SEVERE, "Failed to send destroy packet", e);
                }
              });
    }
  }

  @Override
  public @NonNull LegacyAdaptedBossBar display() {
    if (this.isDestroyed()) {
      return this;
    }
    Optional<Player> owner = this.getOwnerBukkit();
    if (owner.isPresent()) {
      if (!this.isDisplayed()) {
        this.displayWither(owner.get());
      }
    } else {
      this.destroy();
    }
    return this;
  }

  private void displayWither(@NonNull Player player) {
    try {
      Location location = player.getLocation();
      Location witherLocation = LegacyAdaptedBossBar.getWitherLocation(location);
      WrappedWorldServer world = WrappedCraftWorld.of(player.getWorld()).getHandle();
      wither = WrappedEntityWither.construct(world);
      wither.setCustomName(title);
      wither.setInvisible(true);
      wither.setHealth(progress * wither.getMaxHealth());
      wither.setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
      Packet packet =
          PacketType.Play.ClientBound.SPAWN_ENTITY_LIVING.create(
              ReflectUtil.nonNullWrapped(
                  wither, new PacketHandlingException("Wither could not be created successfully")));
      packet.send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().getLogger().log(Level.SEVERE, "Failed to send spawn packet", e);
      this.destroyed = true;
    }
  }
}
