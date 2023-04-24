package com.github.chevyself.babel.api.scoreboard;

import java.util.List;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Represents the scoreboard of a channel. */
public interface ChannelScoreboard {

  /**
   * Updates the scoreboard.
   *
   * <p>This means that the text in the scoreboard lines will be refreshed.
   *
   * @return this instance
   */
  @NonNull
  ChannelScoreboard update();

  /**
   * Updates the scoreboard at a specific position.
   *
   * @see #update()
   * @param position the position of the line to update
   * @return this instance
   */
  @NonNull
  ChannelScoreboard update(int position);

  /**
   * Shows the scoreboard to the player.
   *
   * @param title the title of the scoreboard
   * @return this instance
   */
  @NonNull
  ChannelScoreboard initialize(@Nullable String title);

  /**
   * Get the current layout of the scoreboard.
   *
   * @return the current layout of the scoreboard
   */
  @NonNull
  List<ScoreboardLine> getLayout();

  /**
   * Set the layout of the scoreboard.
   *
   * @param layout the new layout of the scoreboard
   * @return this instance
   */
  @NonNull
  ChannelScoreboard setLayout(@NonNull List<ScoreboardLine> layout);
}
