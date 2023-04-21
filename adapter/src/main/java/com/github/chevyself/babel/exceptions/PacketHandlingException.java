package com.github.chevyself.babel.exceptions;

import lombok.NonNull;

/** This exception is thrown when an error occurs while handling a packet or nms classes. */
public final class PacketHandlingException extends Exception {

  /**
   * Create a new exception.
   *
   * @param message the message of the exception
   * @param cause the cause of the exception
   */
  public PacketHandlingException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a new exception.
   *
   * @param message the message of the exception
   */
  public PacketHandlingException(@NonNull String message) {
    super(message);
  }
}
