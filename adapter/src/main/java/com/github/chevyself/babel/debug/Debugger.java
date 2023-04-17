package com.github.chevyself.babel.debug;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;

/**
 * This class is used to handle errors and debug messages.
 *
 * <p>A default instance is provided by {@link LoggerDebugger} which only prints the messages to the
 * console.
 */
public abstract class Debugger {

  private static Debugger instance;

  /**
   * Get the instance of the debugger.
   *
   * @return the instance
   * @throws NullPointerException if the instance has not been initialized
   */
  @NonNull
  public static Debugger getInstance() {
    return Objects.requireNonNull(Debugger.instance, "Error handler has not been initialized");
  }

  /**
   * Set the instance of the debugger.
   *
   * @param instance the instance to set
   * @return the instance
   */
  @NonNull
  public static Debugger setInstance(@NonNull Debugger instance) {
    Debugger.instance = instance;
    return Debugger.instance;
  }

  /**
   * Set the default instance of the debugger.
   *
   * <p>This instance is {@link LoggerDebugger} which only prints the messages to the console.
   */
  public static void setDefaultInstance() {
    Debugger.instance = new LoggerDebugger(Logger.getLogger("Babel"));
  }

  /**
   * Get the logger of the debugger.
   *
   * @return the logger
   */
  @NonNull
  public abstract Logger getLogger();

  @Deprecated
  public abstract void handle(
      @NonNull Level level, @NonNull String message, @NonNull Throwable cause);

  @Deprecated
  public abstract void handle(@NonNull Level level, @NonNull String message);

  public static class LoggerDebugger extends Debugger {

    @NonNull @Getter private final Logger logger;

    public LoggerDebugger(@NonNull Logger logger) {
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
}
