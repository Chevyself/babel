package com.github.chevyself.babel.commands;

import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.commands.bossbar.BossbarCommands;
import com.github.chevyself.babel.commands.sound.LatestPlaySoundCommands;
import com.github.chevyself.babel.commands.sound.LegacyPlaySoundCommands;
import com.github.chevyself.babel.commands.tab.TabCommands;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.bukkit.AnnotatedCommand;
import com.github.chevyself.starbox.bukkit.CommandManager;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import java.util.Collection;
import lombok.NonNull;

public class BabelCommand {

  @NonNull
  public static Collection<AnnotatedCommand> getCommands(@NonNull CommandManager manager) {
    Collection<AnnotatedCommand> commands = manager.parseCommands(new BabelCommand());
    AnnotatedCommand babel = BabelCommand.getBabel(commands);
    manager.parseCommands(BabelCommand.getSoundCommands()).forEach(babel::addChildren);
    manager.parseCommands(new TabCommands()).forEach(babel::addChildren);
    manager.parseCommands(new ChannelCommands()).forEach(babel::addChildren);
    manager.parseCommands(new ScoreboardCommands()).forEach(babel::addChildren);
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
  private static AnnotatedCommand getBabel(@NonNull Collection<AnnotatedCommand> commands) {
    return commands.stream()
        .filter(annotated -> annotated.getName().equals("babel"))
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  @Parent
  @Command(
      aliases = {"babel", "bbl", "chat"},
      description = "Babel parent command",
      permission = "babel.main")
  public Text babel(
      @Required(
              name = "text",
              description = "The text to parse",
              suggestions = {"$"},
              behaviour = ArgumentBehaviour.CONTINUOUS)
          Text text) {
    return text;
  }

  @Command(aliases = "version", description = "Get the version of Babel", permission = "babel.main")
  public Text version() {
    return Text.localized("cmd.version");
  }
}
