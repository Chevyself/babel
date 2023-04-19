package com.github.chevyself.reflect.debug.tasks;

import com.github.chevyself.reflect.debug.Debugger;
import lombok.NonNull;

/**
 * Represents a task that is being debugged.
 */
public interface DebugTask {

    /**
     * Get the id of the task.
     *
     * @return the id of the task.
     */
    int getId();

    /**
     * Get the debugger that is debugging this task.
     *
     * @return the debugger that is debugging this task
     */
    @NonNull
    Debugger getDebugger();

    /**
     * Start the task.
     *
     * @param message The message to log
     * @return the task
     */
    @NonNull
    default DebugTask start(@NonNull String message) {
        this.getDebugger().getLogger().fine("Starting task #" + this.getId() + ": " + message);
        return this;
    }

    /**
     * End the task.
     *
     * @param message the message to log
     * @return the task
     */
    default DebugTask end(@NonNull String message) {
        this.getDebugger().getLogger().fine("Ending task #" + this.getId() + ": " + message);
        return this;
    }
}
