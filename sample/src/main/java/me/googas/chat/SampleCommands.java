package me.googas.chat;

import java.util.Locale;
import me.googas.chat.api.Channel;
import me.googas.chat.api.Language;
import me.googas.chat.api.lines.Line;
import me.googas.commands.annotations.Free;
import me.googas.commands.annotations.Parent;
import me.googas.commands.annotations.Required;
import me.googas.commands.arguments.ArgumentBehaviour;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SampleCommands {

  @Parent
  @Command(
      aliases = "chat",
      description = "Parent command for the commands of Chat sample plugin",
      permission = "chat.main")
  public BukkitResult chat(
      CommandContext context,
      @Required(
              name = "line",
              behaviour = ArgumentBehaviour.CONTINUOUS,
              suggestions = {"$", "$cmd.hello"})
          String line) {
    Locale locale = Language.getLocale(context.getSender());
    return Line.parse(locale, line).formatSample(locale);
  }

  @Command(aliases = "sound", description = "a")
  public void sound(
      Player player,
      Channel channel,
      @Required(name = "sound", description = "The sound to play") Sound sound,
      @Free(name = "volume", description = "The volume to play the sound", suggestions = "1")
          int volume,
      @Free(name = "pitch", description = "The pitch of the sound", suggestions = "1") int pitch) {
    channel.playSound(player.getLocation(), sound, volume, pitch);
  }

  @Command(aliases = "title", description = "Send a title", permission = "chat.title")
  public void title(
      Channel channel,
      @Required(
              name = "title",
              description = "The title to show",
              behaviour = ArgumentBehaviour.MULTIPLE)
          String title,
      @Required(
              name = "subtitle",
              description = "The subtitle to show",
              behaviour = ArgumentBehaviour.MULTIPLE)
          String subtitle,
      @Free(
              name = "fade in",
              description = "The time that the title will have to show",
              suggestions = "20")
          int fadeIn,
      @Free(name = "stay", description = "The time that the title will stay", suggestions = "20")
          int stay,
      @Free(
              name = "fade in",
              description = "The time that the title will have to show",
              suggestions = "20")
          int fadeOut) {
    channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Command(aliases = "tab", permission = "chat.tab")
  public BukkitResult tab(
      Channel channel,
      @Required(name = "header", behaviour = ArgumentBehaviour.MULTIPLE) String header,
      @Required(name = "footer", behaviour = ArgumentBehaviour.MULTIPLE) String footer) {
    channel.setTabList(header, footer);
    return Line.localized("cmd.tab").format(header, footer);
  }
}
