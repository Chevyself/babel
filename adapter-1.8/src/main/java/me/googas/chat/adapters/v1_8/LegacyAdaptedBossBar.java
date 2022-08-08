package me.googas.chat.adapters.v1_8;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.entity.WrappedDataWatcher;
import me.googas.chat.packet.entity.WrappedEntity;
import me.googas.chat.packet.entity.WrappedEntityWither;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LegacyAdaptedBossBar implements AdaptedBossBar {

  @NonNull @Getter private final UUID owner;
  @NonNull private final WrappedEntityWither wither;
  @Getter private boolean destroyed;
  private String title;
  private float progress = 1;

  LegacyAdaptedBossBar(@NonNull UUID owner, @NonNull WrappedEntityWither wither) {
    this.owner = owner;
    this.wither = wither;
  }

  @Override
  public void setTitle(@NonNull String title) {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      this.title = title;
      this.callMetadataPacket(bukkit.get());
    } else {
      destroy();
    }
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
      ErrorHandler.getInstance().handle(Level.SEVERE, "Failed to send metadata packet", e);
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
    return dataWatcher.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public void setProgress(float progress) {
    if (progress <= 0) {
      this.destroy();
    } else {
      Optional<Player> bukkit = this.getOwnerBukkit();
      if (bukkit.isPresent()) {
        this.progress = progress;
        this.callMetadataPacket(bukkit.get());
      } else {
        destroy();
      }
    }
  }

  public void teleport() {
    Optional<Player> bukkit = this.getOwnerBukkit();
    if (bukkit.isPresent()) {
      try {
        Player player = bukkit.get();
        Location location = LegacyBossBarAdapter.getWitherLocation(player.getLocation());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        Packet packet =
            PacketType.Play.ClientBound.ENTITY_TELEPORT.create(
                new Class[] {WrappedEntity.CLAZZ.getClazz()},
                wither
                    .get()
                    .orElseThrow(
                        () -> new PacketHandlingException("Wither is no longer reachable")));
        packet.send(player);
      } catch (PacketHandlingException e) {
        ErrorHandler.getInstance().handle(Level.SEVERE, "Failed to send teleport packet");
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
                  ErrorHandler.getInstance()
                      .handle(Level.SEVERE, "Failed to destroy wither entity", e);
                }
              });
      wither.set(null);
    }
  }

  private interface WitherRunnable {
    void run() throws PacketHandlingException;
  }
}
