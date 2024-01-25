package com.github.chevyself.babel;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.lang.Language;
import com.github.chevyself.babel.api.scoreboard.ScoreboardLine;
import com.github.chevyself.babel.api.tab.TabCoordinate;
import com.github.chevyself.babel.api.tab.TabView;
import com.github.chevyself.babel.api.tab.entries.CoordinateTabEntry;
import com.github.chevyself.babel.api.tab.entries.PlayerTabEntry;
import com.github.chevyself.babel.api.text.PlainText;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.CommandPermission;
import com.github.chevyself.starbox.result.Result;
import java.util.*;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@SuppressWarnings("MissingJavadoc")
@Deprecated
public class SampleCommands {

  @Command(aliases = "bossbar")
  public Result bossbar(
      Channel channel,
      @Required(name = "progress") double progress,
      @Required(name = "text", behaviour = ArgumentBehaviour.CONTINUOUS) String text) {
    channel.getBossBar().setTitle(text).setProgress((float) progress).display();
    return Text.of("Given boss bar");
  }

  @Command(aliases = "scoreboard")
  public Result scoreboard(Channel channel) {
    List<ScoreboardLine> layout =
        ScoreboardLine.parse(Arrays.asList("This", "is", "the", "best", ":)"), false);
    channel.getScoreboard().setLayout(layout).initialize("Hey!");
    return Text.of("Applied");
  }

  @Command(aliases = "ctabsort")
  public Result ctabSort(Channel channel) {
    TabView tabView = channel.getTabView();
    tabView.sort();
    return Text.of("Sorted");
  }

  @Parent
  @CommandPermission("chat.main")
  @Command(aliases = "chat", description = "Parent command for the commands of Chat sample plugin")
  public Result chat(
      CommandContext context,
      @Required(
              name = "line",
              behaviour = ArgumentBehaviour.CONTINUOUS,
              suggestions = {"$", "$cmd.hello"})
          String line) {
    Locale locale = Language.getLocale(context.getSender());
    return Text.parse(locale, line);
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

  @Command(aliases = "ctab", description = "tests")
  public void ctab(Channel channel) {
    channel.getTabView().initialize();
  }

  @Command(aliases = "ctabclear", description = "tests")
  public PlainText ctabclear(Channel channel) {
    channel.getTabView().clear();
    return Text.of("Done");
  }

  @Command(aliases = "ctabadd")
  public Text ctabadd(Channel channel, Player player) {
    if (channel.hasTabView()) {
      channel.getTabView().add(new PlayerTabEntry(player));
      return Text.of("Done");
    } else {
      return Text.of("No view");
    }
  }

  @Command(aliases = "ctabremove")
  public Text ctabremove(Channel channel, Player player) {
    if (channel.hasTabView()) {
      channel.getTabView().remove(new PlayerTabEntry(player));
      return Text.of("Done");
    } else {
      return Text.of("No view");
    }
  }

  @Command(aliases = "ctabcoords")
  public Text ctabcoords(Channel channel, Player player) {
    if (channel.hasTabView()) {
      TabView tabView = channel.getTabView();
      for (TabCoordinate coordinate : tabView.getSize()) {
        //noinspection deprecation Test command, don't worry
        tabView.set(coordinate, new CoordinateTabEntry());
      }
      return Text.of("Done");
    } else {
      return Text.of("No view");
    }
  }

  @CommandPermission("chat.title")
  @Command(aliases = "title", description = "Send a title")
  public void title(
      Channel channel,
      @Required(name = "title", description = "The title to show") String title,
      @Required(name = "subtitle", description = "The subtitle to show") String subtitle,
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
    channel.sendRawTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @CommandPermission("chat.tab")
  @Command(aliases = "tab")
  public Result tab(
      Channel channel,
      @Required(name = "header") String header,
      @Required(name = "footer") String footer) {
    channel.setRawTabList(header, footer);
    return Text.localized("cmd.tab").format(header, footer);
  }
}
