package com.github.chevyself.reflect.debug.tasks;

import com.github.chevyself.reflect.debug.Debugger;
import lombok.NonNull;

/**
 * Represents a task that has a number of steps to complete.
 *
 */
public class PercentageTask extends TimeTask {

    /**
     * The total steps to complete.
     */
    private final int size;
    /**
     * The number of steps completed.
     */
    private int completed;

    /**
     * Creates a new PercentageTask.
     *
     * @param id The id of the task
     * @param debugger the debugger
     * @param size the total number of steps to complete.
     */
    public PercentageTask(int id, @NonNull Debugger debugger, int size) {
        super(id, debugger);
        this.size = size;
    }

    /**
     * Logs a step completed.
     *
     * @param message the message to log
     */
    public void lap(@NonNull String message) {
        completed++;
        this.debugger.getLogger().finest(getBar() + " " + completed + "/" + size + " " + message);
    }

    @NonNull
    private String getBar() {
        return "[" + DebugUtil.percentageBar(20, size, completed) + "]";
    }

    @Override
    public @NonNull PercentageTask start(@NonNull String message) {
        return (PercentageTask) super.start(message);
    }
}
