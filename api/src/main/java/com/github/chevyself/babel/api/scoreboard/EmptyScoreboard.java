package com.github.chevyself.babel.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Represents a scoreboard without a player. */
public class EmptyScoreboard implements ChannelScoreboard {
  @Override
  public @NonNull EmptyScoreboard update() {
    return this;
  }

  @Override
  public @NonNull EmptyScoreboard update(int position) {
    return this;
  }

  @Override
  public @NonNull EmptyScoreboard initialize(@Nullable String title) {
    return this;
  }

  @Override
  public @NonNull List<ScoreboardLine> getLayout() {
    return new ArrayList<>();
  }

  @Override
  public @NonNull EmptyScoreboard setLayout(@NonNull List<ScoreboardLine> layout) {
    return this;
  }
}
