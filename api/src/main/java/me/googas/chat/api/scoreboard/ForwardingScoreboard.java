package me.googas.chat.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public interface ForwardingScoreboard extends ChannelScoreboard {

  @Override
  @NonNull
  default ChannelScoreboard update() {
    this.getForwards().forEach(ChannelScoreboard::update);
    return this;
  }

  @Override
  @NonNull
  default ChannelScoreboard update(int position) {
    this.getForwards().forEach(scoreboard -> scoreboard.update(position));
    return this;
  }

  @Override
  @NonNull
  default ChannelScoreboard setLayout(@NonNull List<ScoreboardLine> layout) {
    this.getForwards().forEach(scoreboard -> scoreboard.setLayout(layout));
    return this;
  }

  @NonNull
  List<? extends ChannelScoreboard> getForwards();

  @Override
  @NonNull
  default ChannelScoreboard initialize(String title) {
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
