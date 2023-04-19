package com.github.chevyself.reflect.debug.tasks;

import com.github.chevyself.reflect.debug.Debugger;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a task that is timed.
 *
 * <p>On {@link #start(String)} the time is recorded and on {@link #end(String)} the time is calculated
 */
public class TimeTask implements DebugTask {

    @NonNull
    @Getter
    protected final Debugger debugger;
    @Getter
    private final int id;
    protected long start;

    /**
     * Create a new time task.
     *
     * @param id the id of the task
     * @param debugger the debugger
     */
    public TimeTask(int id, @NonNull Debugger debugger) {
        this.id = id;
        this.debugger = debugger;
        this.start = 0;
    }

    @Override
    public @NonNull TimeTask start(@NonNull String message) {
        this.start = System.currentTimeMillis();
        return (TimeTask) DebugTask.super.start(message);
    }

    @Override
    public DebugTask end(@NonNull String message) {
        this.debugger.getLogger().fine("Finished task #" + this.id + ": " + message + " in " + (System.currentTimeMillis() - this.start) + "ms");
        return this;
    }
}
