package com.github.chevyself.reflect.debug.tasks;

import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.debug.LogTask;
import lombok.NonNull;

/**
 * Represents a task that is timed.
 *
 * <p>On {@link #start(String)} the time is recorded and on {@link #end(String)} the time is
 * calculated
 */
public class TimeTask extends LogTask {
  protected long start;

  /**
   * Create a new time task.
   *
   * @param id the id of the task
   * @param debugger the debugger
   */
  public TimeTask(int id, @NonNull Debugger debugger) {
    super(id, debugger);
    this.start = 0;
  }

  @Override
  public @NonNull TimeTask start(@NonNull String message) {
    this.start = System.currentTimeMillis();
    return (TimeTask) super.start(message);
  }

  @Override
  public LogTask end(@NonNull String message) {
    this.debugger
        .getLogger()
        .fine(
            "Finished task #"
                + this.id
                + ": "
                + message
                + " in "
                + (System.currentTimeMillis() - this.start)
                + "ms");
    return this;
  }
}
