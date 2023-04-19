package com.github.chevyself.reflect.debug.tasks;

import lombok.NonNull;

/**
 * Utility class for debugging.
 */
final class DebugUtil {

    /**
     * Calculates percentage.
     *
     * @param total the total
     * @param completed the completed
     * @return the percentage
     */
    public static float getPercentage(int total, int completed) {
        return (float) completed / total;
    }

    /**
     * Creates a percentage bar.
     *
     * @param size the size of the bar
     * @param total the total
     * @param completed the completed
     * @param completedChar the character to use for completed
     * @param uncompletedChar the character to use for uncompleted
     * @return the percentage bar
     */
    @NonNull
    public static String percentageBar(int size, int total, int completed, char completedChar, char uncompletedChar) {
        StringBuilder builder = new StringBuilder();
        float percentage = DebugUtil.getPercentage(total, completed);
        for (int i = 0; i < size; i++) {
            if (i < (size * percentage)) {
                builder.append(completedChar);
            } else {
                builder.append(uncompletedChar);
            }
        }
        return builder.toString();
    }

    /**
     * Creates a percentage bar.
     *
     * <p>Uses {@code █} for completed and {@code ░} for uncompleted.</p>
     *
     * @see #percentageBar(int, int, int, char, char)
     * @param size the size of the bar
     * @param total the total
     * @param completed the completed
     * @return the percentage bar
     */
    @NonNull
    public static String percentageBar(int size, int total, int completed) {
        return percentageBar(size, total, completed, '█', '░');
    }
}
