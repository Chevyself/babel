package com.github.chevyself.babel.api.scoreboard;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.LocalizedTextReference;
import com.github.chevyself.babel.api.text.Text;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;

/**
 * Represents a line in a scoreboard layout. The layout is represented by a list of lines.
 *
 * <p>For instance:
 *
 * <ul>
 *   <li>
 *   <li>Hello,
 *   <li>World!
 *   <li>
 * </ul>
 *
 * <p>Would be represented by a list of 4 lines, the first and last being empty. To do parse layouts
 * use the method: {@link ScoreboardLine#parse(List, boolean)}.
 *
 * <p>You could also parse a single line using {@link ScoreboardLine#parse(String, int)}.
 */
public class ScoreboardLine {

  @NonNull @Getter private final Text text;
  @Getter private final int position;

  /**
   * Create a new scoreboard line.
   *
   * @param text the text of the line
   * @param position the position of the line
   */
  public ScoreboardLine(@NonNull Text text, int position) {
    this.text = text;
    this.position = position;
  }

  /**
   * Parse a list of strings into a list of scoreboard lines. The list of scoreboard lines can are
   * used as a layout for a scoreboard.
   *
   * <p>The second parameter can be used to reverse the order of the lines. This is useful when
   * parsing a layout from a file, since the lines are read from top to bottom.
   *
   * @param layout the layout to parse
   * @param reverse whether to reverse the order of the lines
   * @return the parsed lines
   */
  @NonNull
  public static List<ScoreboardLine> parse(@NonNull List<String> layout, boolean reverse) {
    List<ScoreboardLine> lines = new ArrayList<>();
    if (reverse) Collections.reverse(layout);
    for (int position = 0; position < layout.size(); position++) {
      lines.add(ScoreboardLine.parse(layout.get(position), position));
    }
    return lines;
  }

  /**
   * Parse a string into a scoreboard line.
   *
   * @see Text#parse(String)
   * @param string the string to parse as {@link Text}
   * @param position the position of the line
   * @return the parsed line
   */
  @NonNull
  public static ScoreboardLine parse(@NonNull String string, int position) {
    return new ScoreboardLine(Text.parse(string), position);
  }

  /**
   * Build this line for a player.
   *
   * <p>This method will parse the text of the line, and replace any placeholders with the
   * appropriate values.
   *
   * <p>This will set the field {@link #text} into a sample and with placeholders.
   *
   * @see Text#setSample(boolean)
   * @see Text#setHasPlaceholders(boolean)
   * @see Text#asString()
   * @param player the player to build the line for
   * @return the built line
   */
  @NonNull
  public String build(@NonNull OfflinePlayer player) {
    Channel channel = Channel.of(player);
    Text text = this.text;
    if (this.text instanceof LocalizedTextReference) {
      text = ((LocalizedTextReference) this.text).asLocalized(channel);
    }
    return text.setSample(true).setHasPlaceholders(true).asString(channel);
  }
}
