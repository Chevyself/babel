package com.github.chevyself.babel.exceptions;

import lombok.NonNull;

public final class PacketHandlingException extends Exception {
  public PacketHandlingException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public PacketHandlingException(@NonNull String message) {
    super(message);
  }
}
