package com.github.chevyself.reflect.debug;

import com.github.chevyself.reflect.debug.tasks.DebugTask;
import com.github.chevyself.reflect.debug.tasks.PercentageTask;
import com.github.chevyself.reflect.debug.tasks.TimeTask;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;

/**
 * Debugging tool used to track tasks.
 *
 * <p>This is a factory class that creates {@link DebugTask} objects. Which can be used to measure
 * time, track process and output relevant information of the code that is being executed.
 */
public final class Debugger {

  @NonNull private static Debugger instance = new Debugger(Logger.getLogger("Reflect"));
  @NonNull @Getter private final Logger logger;
  private int taskCount = 1;

  /**
   * Create the debugger with the given logger.
   *
   * @param logger the logger to use
   */
  public Debugger(@NonNull Logger logger) {
    this.logger = logger;
  }

  @NonNull
  public static Debugger getInstance() {
    return instance;
  }

  public static void setInstance(@NonNull Debugger instance) {
    Debugger.instance = instance;
  }

  /**
   * Create a new {@link TimeTask} with the given message.
   *
   * @param message the message to display
   * @return the new task
   */
  @NonNull
  public DebugTask timeTask(@NonNull String message) {
    return new TimeTask(taskCount++, this).start(message);
  }

  /**
   * Create a new {@link PercentageTask} with the given message and size.
   *
   * @param message the message to display
   * @param size the size of the task
   * @return the new task
   */
  @NonNull
  public PercentageTask percentageTask(@NonNull String message, int size) {
    return new PercentageTask(taskCount++, this, size).start(message);
  }

  /**
   * Check if the two variables are equal.
   *
   * @param message the message to display
   * @param a the first variable
   * @param b the second variable
   * @param all if true, the message will display the variables and if they are equal. If false, the
   *     message will only display if the variables are equal.
   */
  public void varCheck(@NonNull String message, @NonNull Object a, Object b, boolean all) {
    if (all) {
      logger.finest(message + " a: '" + a + "' b: '" + b + "' match: " + a.equals(b));
    } else {
      logger.finest(message + " found match: a:" + a + " b:" + b);
    }
  }

  public LogTask logTask(@NonNull String message) {
    return new LogTask(taskCount++, this).start(message);
  }
}
