package com.github.chevyself.babel.commands.bossbar;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.bukkit.AnnotatedCommand;
import com.github.chevyself.starbox.bukkit.CommandManager;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import java.util.Collection;
import lombok.NonNull;

public class BossbarCommands {

  @NonNull
  public static Collection<AnnotatedCommand> getBossbarCommands(CommandManager manager) {
    Collection<AnnotatedCommand> commands = manager.parseCommands(new BossbarCommands());
    if (Versions.getBukkit().isAfter(8)) {
      AnnotatedCommand bossbar =
          commands.stream()
              .filter(command -> command.getName().equals("bossbar"))
              .findFirst()
              .orElseThrow(NullPointerException::new);
      manager.parseCommands(new LatestBossbarCommands()).forEach(bossbar::addChildren);
    }
    return commands;
  }

  @Parent
  @Command(aliases = "bossbar", description = "Create a bossbar", permission = "babel.bossbar")
  public Text bossbar(
      Channel channel,
      @Required(name = "progress", description = "The progress of the bossbar", suggestions = "1")
          double progress,
      @Required(name = "title", description = "The title of the bossbar") Text title) {
    channel.getBossBar().setTitle(title.asString(channel)).setProgress((float) progress);
    return Text.of("Bossbar created");
  }

  @Command(
      aliases = "title",
      description = "Set the title of the bossbar",
      permission = "babel.bossbar.title")
  public Text title(
      Channel channel,
      @Required(name = "title", description = "The title of the bossbar") Text title) {
    channel.getBossBar().setTitle(title.asString(channel));
    return Text.of("Bossbar title set");
  }

  @Command(
      aliases = "progress",
      description = "Set the progress of the bossbar",
      permission = "babel.bossbar.progress")
  public Text progress(
      Channel channel,
      @Required(name = "progress", description = "The progress of the bossbar", suggestions = "1")
          double progress) {
    channel.getBossBar().setProgress((float) progress);
    return Text.of("Bossbar progress set");
  }

  @Command(
      aliases = "display",
      description = "Display the bossbar",
      permission = "babel.bossbar.display")
  public Text display(Channel channel) {
    channel.getBossBar().display();
    return Text.of("Bossbar displayed");
  }
}
