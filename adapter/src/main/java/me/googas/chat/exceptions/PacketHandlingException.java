package me.googas.chat.exceptions;

import lombok.NonNull;

public final class PacketHandlingException extends Exception {
  public PacketHandlingException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}
