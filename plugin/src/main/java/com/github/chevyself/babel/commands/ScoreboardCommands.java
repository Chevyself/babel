package com.github.chevyself.babel.commands;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.scoreboard.ScoreboardLine;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;

public class ScoreboardCommands {

  @Parent
  @Command(
      aliases = "scoreboard",
      description = "Starts the scoreboard for the player",
      permission = "babel.scoreboard")
  public Text scoreboard(
      Channel channel,
      @Required(
              name = "title",
              description = "The title of the scoreboard",
              behaviour = ArgumentBehaviour.MULTIPLE)
          Text title,
      @Required(
              name = "layout",
              description = "The layout of the scoreboard",
              behaviour = ArgumentBehaviour.CONTINUOUS)
          String layout) {
    channel.getScoreboard().setLayout(this.getLayout(layout)).initialize(title.asString(channel));
    return Text.of("Scoreboard set!");
  }

  @NonNull
  public List<ScoreboardLine> getLayout(@NonNull String layout) {
    return ScoreboardLine.parse(Arrays.asList(layout.split("\\|")), true);
  }
}
