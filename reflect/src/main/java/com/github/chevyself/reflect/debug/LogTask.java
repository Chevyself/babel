package com.github.chevyself.reflect.debug;

import com.github.chevyself.reflect.debug.tasks.DebugTask;
import lombok.Getter;
import lombok.NonNull;

/** Represents a task that has messages to be logged. */
public class LogTask implements DebugTask {

  @Getter protected final int id;
  @Getter protected final Debugger debugger;

  /**
   * Creates a new log task.
   *
   * @param id the id of the task
   * @param debugger the debugger that created the task
   */
  public LogTask(int id, Debugger debugger) {
    this.id = id;
    this.debugger = debugger;
  }

  @Override
  public @NonNull LogTask start(@NonNull String message) {
    return (LogTask) DebugTask.super.start(message);
  }

  @Override
  public LogTask end(@NonNull String message) {
    return (LogTask) DebugTask.super.end(message);
  }
}
