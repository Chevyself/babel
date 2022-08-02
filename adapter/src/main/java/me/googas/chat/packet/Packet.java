package me.googas.chat.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.entity.player.WrappedCraftPlayer;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** Represents a Packet which can be sent to a player or may be sent by a player. */
public final class Packet extends SimpleWrapper<Object> {

  @NonNull
  public static final String NMS = Bukkit.getServer().getClass().getCanonicalName().split("\\.")[3];

  @NonNull
  public static final WrappedClass<?> PACKET_CLASS =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".Packet");

  @NonNull @Getter private final PacketType type;
  @NonNull @Getter private final WrappedClass<?> clazz;

  /**
   * Create the packet.
   *
   * @param type the type of the packet
   * @param clazz the class of the type of the packet
   * @param reference the created reference of the packet
   */
  public Packet(@NonNull PacketType type, @NonNull WrappedClass<?> clazz, Object reference) {
    super(reference);
    this.type = type;
    this.clazz = clazz;
  }

  public static Packet forType(
      @NonNull PacketType type, @NonNull Class<?>[] params, Object... objects)
      throws PacketHandlingException {
    WrappedClass<?> clazz = type.wrap();
    Object handle;
    try {
      if (objects.length > 0) {
        if (clazz.hasConstructor(params)) {
          WrappedConstructor<?> constructor = clazz.getConstructor(params);
          handle = constructor.invoke(objects);
        } else {
          throw new PacketHandlingException(
              "Could not find constructor with " + Arrays.toString(params));
        }
      } else {
        handle = clazz.getConstructor().invoke();
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke constructor for packet", e);
    }
    return new Packet(type, clazz, handle);
  }

  public static Packet forType(@NonNull PacketType type, Object... objects)
      throws PacketHandlingException {
    WrappedClass<?> clazz = type.wrap();
    Object handle;
    try {
      if (objects.length > 0) {
        return forType(type, getConstructorParameters(objects), objects);
      } else {
        handle = clazz.getConstructor().invoke();
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke constructor for packet", e);
    }
    return new Packet(type, clazz, handle);
  }

  private static Class<?>[] getConstructorParameters(Object... objects) {
    List<Class<?>> classes = new ArrayList<>();
    for (Object object : objects) {
      if (object != null) {
        classes.add(object.getClass());
      }
    }
    return classes.toArray(new Class[0]);
  }

  /**
   * Edit the packet using its class.
   *
   * @param consumer the consumer to edit the packet
   * @return this same instance
   */
  @NonNull
  public Packet usingClazz(@NonNull BiConsumer<Object, WrappedClass<?>> consumer) {
    consumer.accept(this.reference, this.clazz);
    return this;
  }

  /**
   * Set a field in the packet.
   *
   * @param index the index of the field
   * @param packetData the packet data to set in the field
   * @return this same instance
   */
  @NonNull
  public Packet setField(int index, PacketDataWrapper packetData) throws PacketHandlingException {
    return this.setField(index, packetData == null ? null : packetData.getHandle());
  }

  /**
   * Set a field in the packet.
   *
   * @param index the index of the field
   * @param object the object to set in the field
   * @return this same instance
   */
  @NonNull
  public Packet setField(int index, Object object) throws PacketHandlingException {
    try {
      this.clazz.getDeclaredFields().get(index).set(this.reference, object);
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Could not set field in packet", e);
    }
    return this;
  }

  /**
   * Send this packet to a player.
   *
   * @param player the player to send this packet to
   */
  public void send(@NonNull Player player) throws PacketHandlingException {
    WrappedCraftPlayer.of(player).getHandle().playerConnection().sendPacket(this);
  }

  /**
   * Send this packet to many players.
   *
   * @param players the collection of players to send this packet to
   */
  public void sendAll(@NonNull Collection<? extends Player> players)
      throws PacketHandlingException {
    for (Player player : players) {
      this.send(player);
    }
  }

  /**
   * Send this packet to many players.
   *
   * @param players the array of players to send this packet to
   */
  public void sendAll(@NonNull Player... players) throws PacketHandlingException {
    this.sendAll(Arrays.asList(players));
  }
}
