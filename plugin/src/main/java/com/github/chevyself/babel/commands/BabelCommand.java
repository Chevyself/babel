package com.github.chevyself.babel.commands;

import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.commands.bossbar.BossbarCommands;
import com.github.chevyself.babel.commands.sound.LatestPlaySoundCommands;
import com.github.chevyself.babel.commands.sound.LegacyPlaySoundCommands;
import com.github.chevyself.babel.commands.tab.TabCommands;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.CommandPermission;
import java.util.List;
import lombok.NonNull;

public class BabelCommand {

  @NonNull
  public static List<BukkitCommand> getCommands(
      @NonNull CommandManager<CommandContext, BukkitCommand> manager) {
    List<BukkitCommand> commands =
        manager.getCommandParser().parseAllCommandsFrom(new BabelCommand());
    BukkitCommand babel = BabelCommand.getBabel(commands);
    babel.parseAndAddChildren(
        BabelCommand.getSoundCommands(),
        new TabCommands(),
        new ChannelCommands(),
        new ScoreboardCommands());
    // Adds bossbar commands
    BossbarCommands.getBossbarCommands(manager).forEach(babel::addChildren);
    return commands;
  }

  @NonNull
  private static Object getSoundCommands() {
    if (Versions.getBukkit().isBefore(11)) {
      return new LegacyPlaySoundCommands();
    } else {
      return new LatestPlaySoundCommands();
    }
  }

  @NonNull
  private static BukkitCommand getBabel(@NonNull List<BukkitCommand> commands) {
    return commands.stream()
        .filter(annotated -> annotated.getName().equals("babel"))
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  @Parent
  @CommandPermission("babel.main")
  @Command(
      aliases = {"babel", "bbl", "chat"},
      description = "Babel parent command")
  public Text babel(
      @Required(
              name = "text",
              description = "The text to parse",
              suggestions = {"$"},
              behaviour = ArgumentBehaviour.CONTINUOUS)
          Text text) {
    return text;
  }

  @CommandPermission("babel.main")
  @Command(aliases = "version", description = "Get the version of Babel")
  public Text version() {
    return Text.localized("cmd.version");
  }
}
