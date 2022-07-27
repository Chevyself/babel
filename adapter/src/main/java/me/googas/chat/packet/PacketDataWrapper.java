package me.googas.chat.packet;

import me.googas.chat.exceptions.PacketHandlingException;

/** Wraps data which is held by a {@link java.lang.reflect.Field} in a {@link Packet} */
public interface PacketDataWrapper {
  /**
   * Get the actual data for this wrapper.
   *
   * @return the data
   */
  Object getHandle() throws PacketHandlingException;
}
