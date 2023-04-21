package com.github.chevyself.babel.adapters;

import lombok.NonNull;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

/**
 * This is an adapter to create objectives. This is used to create objectives that are adapted to
 * the server version
 */
public interface ObjectiveAdapter {

  /**
   * Create an objective with the given parameters.
   *
   * @param scoreboard the scoreboard to register the objective
   * @param name the name of the objective
   * @param criteria the criteria of the objective
   * @param display the display of the objective
   * @return the created objective
   * @throws NullPointerException if any of the parameters is null
   */
  @NonNull
  Objective create(
      @NonNull Scoreboard scoreboard,
      @NonNull String name,
      @NonNull String criteria,
      @Nullable String display);
}
