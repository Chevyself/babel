package me.googas.chat.packet;

import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.reflect.Wrapper;
import me.googas.reflect.wrappers.WrappedClass;

/**
 * This class represents some kind of packet. Packet types can be found in <a
 * href="https://wiki.vg/Protocol">the minecraft protocol</a>.
 */
public final class PacketType {

  @NonNull private final String name;

  PacketType(@NonNull String name) {
    this.name = name;
  }

  /**
   * Wraps the class of this packet type.
   *
   * @return the wrapped class
   */
  @NonNull
  public WrappedClass<?> wrap() {
    return WrappedClass.forName(this.getCanonicalName());
  }

  /**
   * Create a new packet with this type.
   *
   * @return the new packet
   */
  @NonNull
  public Packet create() throws PacketHandlingException {
    return Packet.forType(this);
  }

  /**
   * Get the canonical name of the class for this type.
   *
   * @see Class#getCanonicalName()
   * @return the canonical name for this class
   */
  @NonNull
  public String getCanonicalName() {
    return "net.minecraft.server." + Packet.NMS + "." + name;
  }

  /**
   * Create a new packet with this type and the given objects.
   *
   * @param objects the objects to pass to the constructor
   * @return the new packet
   * @throws PacketHandlingException if the constructor of the packet could not be invoked
   */
  @NonNull
  public Packet create(Object... objects) throws PacketHandlingException {
    return Packet.forType(this, objects);
  }

    /**
     * Create a new packet with this type and the given wrappers.
     *
     * @param wrappers the wrappers to pass to the constructor
     * @return the new packet
     * @throws PacketHandlingException if the constructor of the packet could not be invoked
     */
  @NonNull
  public Packet create(@NonNull Wrapper<?>... wrappers) throws PacketHandlingException {
    Object[] objects = new Object[wrappers.length];
    for (int i = 0; i < wrappers.length; i++) {
      objects[i] = wrappers[i].getWrapped();
    }
    return Packet.forType(this, objects);
  }

    /**
     * Create a new packet with this type and the given objects.
     *
     * @param objects the objects to pass to the constructor
     * @return the new packet
     * @throws PacketHandlingException if the constructor of the packet could not be invoked
     * @deprecated use {@link #create(Object...)} instead
     */
  @Deprecated
  public @NonNull Packet create(@NonNull Class<?>[] params, @NonNull Object... objects)
      throws PacketHandlingException {
    return Packet.forType(this, params, objects);
  }

  /** Packets while in game. */
  public static class Play {

    /** Packets send from the server to the player. */
    public static class ClientBound {

      /** Sends titles and subtitles to the player. */
      @NonNull public static final PacketType TITLE = new PacketType("PacketPlayOutTitle");
      /** Sends a header and a footer to the player. */
      @NonNull
      public static final PacketType HEADER_FOOTER =
          new PacketType("PacketPlayOutPlayerListHeaderFooter");
      /** Spawns an entity for the player. */
      @NonNull
      public static final PacketType SPAWN_ENTITY_LIVING =
          new PacketType("PacketPlayOutSpawnEntityLiving");

      /** Destroys (de-spawns) an entity for the player */
      @NonNull
      public static final PacketType ENTITY_DESTROY = new PacketType("PacketPlayOutEntityDestroy");

      /** Updates the metadata of an entity for the player */
      @NonNull
      public static final PacketType ENTITY_METADATA =
          new PacketType("PacketPlayOutEntityMetadata");

      /** Teleports an entity for the player */
      @NonNull
      public static final PacketType ENTITY_TELEPORT =
          new PacketType("PacketPlayOutEntityTeleport");
      /** Sends information about another player to the player */
      @NonNull
      public static final PacketType PLAYER_INFO = new PacketType("PacketPlayOutPlayerInfo");
    }
  }
}
