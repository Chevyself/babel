package me.googas.chat.api.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.text.LocalizedReference;
import me.googas.chat.api.text.Text;
import org.bukkit.OfflinePlayer;

public class ScoreboardLine {

  @NonNull @Getter private final Text child;
  @Getter private final int position;

  public ScoreboardLine(@NonNull Text child, int position) {
    this.child = child;
    this.position = position;
  }

  @NonNull
  public static List<ScoreboardLine> parse(@NonNull List<String> layout, boolean reverse) {
    List<ScoreboardLine> lines = new ArrayList<>();
    if (reverse) Collections.reverse(layout);
    for (int position = 0; position < layout.size(); position++) {
      lines.add(ScoreboardLine.parse(layout.get(position), position));
    }
    return lines;
  }

  @NonNull
  public static ScoreboardLine parse(@NonNull String string, int position) {
    return new ScoreboardLine(Text.parse(string), position);
  }

  @NonNull
  public String build(@NonNull OfflinePlayer player) {
    Channel channel = Channel.of(player);
    Text text = child;
    if (child instanceof LocalizedReference) {
      text = ((LocalizedReference) child).asLocalized(channel);
    }
    return text.setSample(true).setHasPlaceholders(true).asString(channel);
  }
}
