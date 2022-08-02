package me.googas.chat.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

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
  public @NonNull EmptyScoreboard initialize(String title) {
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
