package me.googas.chat.adapters.v1_8;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.debug.Debugger;
import me.googas.chat.adapters.AdaptedBossBar;
import me.googas.chat.adapters.BossBarAdapter;
import me.googas.chat.adapters.bossbar.EmptyAdaptedBossBar;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.PacketType;
import me.googas.chat.packet.bossbar.WrappedBarColor;
import me.googas.chat.packet.bossbar.WrappedBarStyle;
import me.googas.chat.packet.entity.WrappedEntityLiving;
import me.googas.chat.packet.entity.WrappedEntityWither;
import me.googas.chat.packet.world.WrappedCraftWorld;
import me.googas.chat.packet.world.WrappedWorldServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LegacyBossBarAdapter implements BossBarAdapter {

  @NonNull private final Set<LegacyAdaptedBossBar> bossBars = new HashSet<>();

  static Location getWitherLocation(Location location) {
    return location.add(location.getDirection().multiply(36));
  }

  @Override
  public AdaptedBossBar create(
      @NonNull Player player,
      @NonNull String title,
      float progress,
      WrappedBarColor color,
      WrappedBarStyle style) {
    return null;
  }

  @Override
  public AdaptedBossBar create(@NonNull Player player, @NonNull String title, float progress) {
    try {
      Location location = player.getLocation();
      Location witherLocation = getWitherLocation(location);
      WrappedWorldServer world = WrappedCraftWorld.of(player.getWorld()).getHandle();
      WrappedEntityWither wither = WrappedEntityWither.construct(world);
      wither.setCustomName(title);
      wither.setInvisible(true);
      wither.setHealth(progress * wither.getMaxHealth());
      wither.setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
      Packet packet =
          PacketType.Play.ClientBound.SPAWN_ENTITY_LIVING.create(
              new Class[] {WrappedEntityLiving.CLAZZ.getClazz()},
              wither
                  .get()
                  .orElseThrow(
                      () ->
                          new PacketHandlingException("Wither could not be created successfully")));
      packet.send(player);
      LegacyAdaptedBossBar bossBar = new LegacyAdaptedBossBar(player.getUniqueId(), wither);
      this.bossBars.add(bossBar);
      return bossBar;
    } catch (PacketHandlingException e) {
      Debugger.getInstance().handle(Level.SEVERE, "Failed to create boss bar for player", e);
    }
    return new EmptyAdaptedBossBar(player.getUniqueId());
  }

  @Override
  public @NonNull Optional<LegacyAdaptedBossBar> getBossBar(@NonNull UUID owner) {
    return bossBars.stream()
        .filter(bossBar -> bossBar.getOwner().equals(owner) && !bossBar.isDestroyed())
        .findFirst();
  }
}
