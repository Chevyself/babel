package com.github.chevyself.babel.adapters.scoreboard;

import com.github.chevyself.babel.adapters.ObjectiveAdapter;
import lombok.NonNull;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * This is the latest objective adapter which will use the latest methods to create the objective.
 */
public class LatestObjectiveAdapter implements ObjectiveAdapter {
  @Override
  public @NonNull Objective create(
      @NonNull Scoreboard scoreboard,
      @NonNull String name,
      @NonNull String criteria,
      String display) {
    return scoreboard.registerNewObjective(
        name.length() > 16 ? name.substring(0, 15) : name,
        criteria,
        display == null ? name : display);
  }
}
