package com.github.chevyself.babel.adapters.v1_12;

import com.github.chevyself.babel.adapters.ObjectiveAdapter;
import lombok.NonNull;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

/** An adapter to create objectives in Minecraft 1.12 or previous versions. */
public class LegacyObjectiveAdapter implements ObjectiveAdapter {
  @Override
  public @NonNull Objective create(
      @NonNull Scoreboard scoreboard,
      @NonNull String name,
      @NonNull String criteria,
      @Nullable String display) {
    return scoreboard.registerNewObjective(
        name.length() > 16 ? name.substring(0, 15) : name, criteria);
  }
}
