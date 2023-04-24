package com.github.chevyself.babel.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Represents a scoreboard that forwards all its methods to a list of scoreboards. */
public interface ForwardingScoreboard extends ChannelScoreboard {

  @Override
  @NonNull
  default ForwardingScoreboard update() {
    this.getForwards().forEach(ChannelScoreboard::update);
    return this;
  }

  @Override
  @NonNull
  default ForwardingScoreboard update(int position) {
    this.getForwards().forEach(scoreboard -> scoreboard.update(position));
    return this;
  }

  @Override
  @NonNull
  default ForwardingScoreboard setLayout(@NonNull List<ScoreboardLine> layout) {
    this.getForwards().forEach(scoreboard -> scoreboard.setLayout(layout));
    return this;
  }

  /**
   * Get the scoreboards that this scoreboard forwards to.
   *
   * @return the scoreboards that this scoreboard forwards to
   */
  @NonNull
  List<? extends ChannelScoreboard> getForwards();

  @Override
  @NonNull
  default ForwardingScoreboard initialize(@Nullable String title) {
    this.getForwards()
        .forEach(
            forward -> {
              forward.initialize(title);
            });
    return this;
  }

  @Override
  @NonNull
  default List<ScoreboardLine> getLayout() {
    return new ArrayList<>();
  }
}
