package com.github.chevyself.babel.adapters.v1_8;

import static com.github.chevyself.babel.adapters.v1_8.LegacyBossBarAdapter.getWitherLocation;

import com.github.chevyself.babel.adapters.AdaptedBossBar;
import com.github.chevyself.babel.debug.Debugger;
import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.PacketType;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import com.github.chevyself.babel.packet.entity.WrappedDataWatcher;
import com.github.chevyself.babel.packet.entity.WrappedEntity;
import com.github.chevyself.babel.packet.entity.WrappedEntityLiving;
import com.github.chevyself.babel.packet.entity.WrappedEntityWither;
import com.github.chevyself.babel.packet.world.WrappedCraftWorld;
import com.github.chevyself.babel.packet.world.WrappedWorldServer;
import com.github.chevyself.reflect.util.ReflectUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LegacyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;
  private String title;
  private float progress;
  private WrappedEntityWither wither;
  @Getter private boolean destroyed;

  public LegacyAdaptedBossBar(@NonNull UUID owner) {
    this.owner = owner;
    this.title = "";
    this.progress = 1;
    this.destroyed = false;
  }

  @NonNull
  @Override
  public LegacyAdaptedBossBar setTitle(@NonNull String title) {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      this.title = title;
      if (this.isDisplayed()) {
        this.callMetadataPacket(bukkit.get());
      }
    } else {
      destroy();
    }
    return this;
  }

  private void callMetadataPacket(@NonNull Player player) {
    try {
      Packet packet =
          PacketType.Play.ClientBound.ENTITY_METADATA.create(
              new Class[] {int.class, WrappedDataWatcher.CLAZZ.getClazz(), boolean.class},
              wither.getId(),
              getDataWatcher(),
              true);
      packet.send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().handle(Level.SEVERE, "Failed to send metadata packet", e);
    }
  }

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
    return ReflectUtil.nonNull(
        dataWatcher.getWrapped(),
        new PacketHandlingException("DataWatcher was not created successfully"));
  }

  @NonNull
  @Override
  public LegacyAdaptedBossBar setProgress(float progress) {
    if (progress <= 0) {
      this.destroy();
    } else {
      Optional<Player> bukkit = this.getOwnerBukkit();
      if (bukkit.isPresent()) {
        this.progress = progress;
        if (this.isDisplayed()) {
          this.callMetadataPacket(bukkit.get());
        }
      } else {
        destroy();
      }
    }
    return this;
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

  public void teleport() {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      if (!this.isDisplayed()) {
        return;
      }
      try {
        Player player = bukkit.get();
        Location location = getWitherLocation(player.getLocation());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        Packet packet =
            PacketType.Play.ClientBound.ENTITY_TELEPORT.create(
                new Class[] {WrappedEntity.CLAZZ.getClazz()},
                ReflectUtil.nonNull(
                    wither, new PacketHandlingException("Wither is no longer reachable")));
        packet.send(player);
      } catch (PacketHandlingException e) {
        Debugger.getInstance().handle(Level.SEVERE, "Failed to send teleport packet");
      }
    } else {
      this.destroy();
    }
  }

  @Override
  public void destroy() {
    if (!destroyed) {
      destroyed = true;
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
                  Debugger.getInstance().handle(Level.SEVERE, "Failed to destroy wither entity", e);
                }
              });
      wither.set(null);
    }
  }

  @Override
  public @NonNull LegacyAdaptedBossBar display() {
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
      Location witherLocation = getWitherLocation(location);
      WrappedWorldServer world = WrappedCraftWorld.of(player.getWorld()).getHandle();
      wither = WrappedEntityWither.construct(world);
      wither.setCustomName(title);
      wither.setInvisible(true);
      wither.setHealth(progress * wither.getMaxHealth());
      wither.setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
      Packet packet =
          PacketType.Play.ClientBound.SPAWN_ENTITY_LIVING.create(
              new Class[] {WrappedEntityLiving.CLAZZ.getClazz()},
              ReflectUtil.nonNull(
                  wither, new PacketHandlingException("Wither could not be created successfully")));
      packet.send(player);
    } catch (PacketHandlingException e) {
      Debugger.getInstance().handle(Level.SEVERE, "Failed to create boss bar for player", e);
      this.destroyed = true;
    }
  }
}
