package com.github.chevyself.babel.commands.bossbar;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.CommandPermission;
import java.util.List;
import lombok.NonNull;

public class BossbarCommands {

  @NonNull
  public static List<BukkitCommand> getBossbarCommands(
      CommandManager<CommandContext, BukkitCommand> manager) {
    List<BukkitCommand> commands =
        manager.getCommandParser().parseAllCommandsFrom(new BossbarCommands());
    if (Versions.getBukkit().isAfter(8)) {
      BukkitCommand boosbar =
          commands.stream()
              .filter(command -> command.getName().equals("bossbar"))
              .findFirst()
              .orElseThrow(NullPointerException::new);
      boosbar.parseAndAddChildren(manager, new LatestBossbarCommands());
    }
    return commands;
  }

  @Parent
  @CommandPermission("babel.bossbar")
  @Command(aliases = "bossbar", description = "Create a bossbar")
  public Text bossbar(
      Channel channel,
      @Required(name = "progress", description = "The progress of the bossbar", suggestions = "1")
          double progress,
      @Required(name = "title", description = "The title of the bossbar") Text title) {
    channel.getBossBar().setTitle(title.asString(channel)).setProgress((float) progress);
    return Text.of("Bossbar created");
  }

  @CommandPermission("babel.bossbar.title")
  @Command(aliases = "title", description = "Set the title of the bossbar")
  public Text title(
      Channel channel,
      @Required(name = "title", description = "The title of the bossbar") Text title) {
    channel.getBossBar().setTitle(title.asString(channel));
    return Text.of("Bossbar title set");
  }

  @CommandPermission("babel.bossbar.progress")
  @Command(aliases = "progress", description = "Set the progress of the bossbar")
  public Text progress(
      Channel channel,
      @Required(name = "progress", description = "The progress of the bossbar", suggestions = "1")
          double progress) {
    channel.getBossBar().setProgress((float) progress);
    return Text.of("Bossbar progress set");
  }

  @CommandPermission("babel.bossbar.display")
  @Command(aliases = "display", description = "Display the bossbar")
  public Text display(Channel channel) {
    channel.getBossBar().display();
    return Text.of("Bossbar displayed");
  }
}
