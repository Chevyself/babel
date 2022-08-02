package me.googas.chat.api.scoreboard;

import java.util.List;
import lombok.NonNull;

public interface ChannelScoreboard {

  @NonNull
  ChannelScoreboard update();

  @NonNull
  ChannelScoreboard update(int position);

  @NonNull
  ChannelScoreboard initialize(String title);

  @NonNull
  List<ScoreboardLine> getLayout();

  @NonNull
  ChannelScoreboard setLayout(@NonNull List<ScoreboardLine> layout);
}
