package me.googas.chat;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NonNull;

public abstract class ErrorHandler {

  private static ErrorHandler instance;

  @NonNull
  public static ErrorHandler getInstance() {
    return Objects.requireNonNull(ErrorHandler.instance, "Error handler has not been initialized");
  }

  public static void setDefaultInstance() {
    ErrorHandler.instance = new SystemErrorHandler();
  }

  @NonNull
  public static ErrorHandler setInstance(@NonNull ErrorHandler instance) {
    ErrorHandler.instance = instance;
    return ErrorHandler.instance;
  }

  public abstract void handle(
      @NonNull Level level, @NonNull String message, @NonNull Throwable cause);

  public abstract void handle(@NonNull Level level, @NonNull String message);

  public static class LoggerErrorHandler extends ErrorHandler {

    @NonNull private final Logger logger;

    public LoggerErrorHandler(@NonNull Logger logger) {
      this.logger = logger;
    }

    @Override
    public void handle(@NonNull Level level, @NonNull String message, @NonNull Throwable cause) {
      this.logger.log(level, message, cause);
    }

    @Override
    public void handle(@NonNull Level level, @NonNull String message) {
      this.logger.log(level, message);
    }
  }

  static class SystemErrorHandler extends ErrorHandler {

    @Override
    public void handle(@NonNull Level level, @NonNull String message, @NonNull Throwable cause) {
      this.sendMessage(level, message);
      cause.printStackTrace();
    }

    @Override
    public void handle(@NonNull Level level, @NonNull String message) {
      this.sendMessage(level, message);
    }

    private String prefix(@NonNull Level level) {
      return level + ": ";
    }

    private void sendMessage(@NonNull Level level, @NonNull String message) {
      System.out.println(this.prefix(level) + message);
    }
  }
}
