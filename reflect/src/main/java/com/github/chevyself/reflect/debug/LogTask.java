package com.github.chevyself.reflect.debug;

import com.github.chevyself.reflect.debug.tasks.DebugTask;
import lombok.Getter;
import lombok.NonNull;

public class LogTask implements DebugTask {

  @Getter protected final int id;
  @Getter protected final Debugger debugger;

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
