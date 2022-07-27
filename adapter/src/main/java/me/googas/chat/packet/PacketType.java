package me.googas.chat.packet;

import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
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
    }
  }
}
