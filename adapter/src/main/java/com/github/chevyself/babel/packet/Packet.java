package com.github.chevyself.babel.packet;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.entity.player.WrappedCraftPlayer;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.modifiers.Modifier;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedConstructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/** Represents a Packet which can be sent to a player or may be sent by a player. */
public final class Packet extends ReflectWrapper {

  /** The NMS version of the server. */
  @NonNull
  public static final String NMS = Bukkit.getServer().getClass().getCanonicalName().split("\\.")[3];

  /** The class of the packet. */
  @NonNull
  public static final WrappedClass<?> PACKET_CLASS =
      Versions.wrapNmsClassByName("network.protocol", "Packet");

  @NonNull @Getter private final PacketType type;
  @NonNull @Getter private final WrappedClass<?> clazz;

  /**
   * Create the packet.
   *
   * @param type the type of the packet
   * @param clazz the class of the type of the packet
   * @param reference the created reference of the packet
   */
  private Packet(@NonNull PacketType type, @NonNull WrappedClass<?> clazz, Object reference) {
    super(reference, false);
    this.type = type;
    this.clazz = clazz;
  }

  /**
   * Create a packet.
   *
   * @param type the type of the packet
   * @param params the parameters of the constructor
   * @param objects the objects to pass to the constructor
   * @return the packet
   * @throws PacketHandlingException if the constructor of the packet could not be invoked
   * @deprecated use {@link #forType(PacketType, Object...)} instead. Such method gets the
   *     parameters automatically.
   */
  @Deprecated
  public static @NonNull Packet forType(
      @NonNull PacketType type, @NonNull Class<?>[] params, Object... objects)
      throws PacketHandlingException {
    WrappedClass<?> clazz = type.wrap();
    Object handle;
    try {
      if (objects.length > 0) {
        WrappedConstructor<?> constructor = clazz.getConstructor(params);
        handle = constructor.invoke(objects);
      } else {
        handle = clazz.newInstance();
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke constructor for packet", e);
    }
    return new Packet(type, clazz, handle);
  }

  /**
   * Create a packet.
   *
   * @param type the type of the packet
   * @param objects the objects to pass to the constructor
   * @return the packet
   * @throws PacketHandlingException if the constructor of the packet could not be invoked
   */
  public static @NonNull Packet forType(@NonNull PacketType type, Object... objects)
      throws PacketHandlingException {
    WrappedClass<?> clazz = type.wrap();
    Object handle;
    try {
      if (objects.length > 0) {
        WrappedConstructor<?> constructor =
            clazz.getConstructor(Packet.getConstructorParameters(objects));
        if (constructor.isPresent()) {
          handle = constructor.invoke(objects);
        } else {
          throw new PacketHandlingException("Could not find constructor for packet");
        }
      } else {
        handle = clazz.newInstance();
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke constructor for packet", e);
    }
    return new Packet(type, clazz, handle);
  }

  /**
   * Get the parameters of the constructor of the packet.
   *
   * @param objects the objects to pass to the constructor
   * @return the parameters of the constructor
   */
  @NonNull
  private static Class<?>[] getConstructorParameters(@Nullable Object... objects) {
    List<Class<?>> classes = new ArrayList<>();
    for (Object object : objects) {
      if (object != null) {
        classes.add(object.getClass());
      } else {
        classes.add(null);
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
    consumer.accept(this.wrapped, this.clazz);
    return this;
  }

  @NonNull
  public Packet setField(int index, @NonNull Wrapper<?> data) throws PacketHandlingException {
    return this.setField(index, data.getWrapped());
  }

  /**
   * Set a field in the packet.
   *
   * @param index the index of the field
   * @param modifier to modify the value of the field
   * @return this same instance
   */
  @NonNull
  public Packet setField(int index, @NonNull Modifier modifier) throws PacketHandlingException {
    try {
      this.clazz.getDeclaredFields().get(index).set(this.wrapped, modifier);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new PacketHandlingException("Could not set field in packet", e);
    }
    return this;
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
      this.clazz.getDeclaredFields().get(index).set(this.wrapped, object);
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

  @Override
  public Class<?> getReflectClass() {
    return this.clazz.getWrapped();
  }
}
