package com.github.chevyself.babel.commands.tab;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.tab.TabCoordinate;
import com.github.chevyself.babel.api.tab.TabView;
import com.github.chevyself.babel.api.tab.entries.CoordinateTabEntry;
import com.github.chevyself.babel.api.tab.entries.PlayerTabEntry;
import com.github.chevyself.babel.api.tab.entries.TextTabEntry;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.common.CommandPermission;
import org.bukkit.entity.Player;

public class TabCommands {

  @Parent
  @CommandPermission("babel.tab")
  @Command(aliases = "tab", description = "Parent command to test tab-view features")
  public Text tab(Channel channel) {
    if (!channel.hasTabView()) {
      channel.getTabView().initialize();
      return Text.of("Tab view has been initialized");
    } else {
      return Text.of("Tab view already initialized");
    }
  }

  @CommandPermission("babel.tab")
  @Command(
      aliases = {"coordinates", "coords"},
      description = "Show the coordinates of each slot in the tab-view")
  public Text coords(Channel channel) {
    TabView tabView = channel.getTabView();
    for (TabCoordinate coordinate : tabView.getSize()) {
      //noinspection deprecation Test command, don't worry
      tabView.set(coordinate, new CoordinateTabEntry());
    }
    return Text.of("Coordinates set");
  }

  @CommandPermission("babel.tab")
  @Command(aliases = "clear", description = "Clear the tab-view")
  public Text clear(Channel channel) {
    channel.getTabView().clear();
    return Text.of("Tab view cleared");
  }

  @CommandPermission("babel.tab")
  @Command(aliases = "add", description = "Add a player to the tab-view")
  public Text add(
      Channel channel,
      @Required(name = "player", description = "The player to add") Player player) {
    channel.getTabView().add(new PlayerTabEntry(player));
    return Text.of("Player added");
  }

  @CommandPermission("babel.tab")
  @Command(aliases = "remove", description = "Remove a player from the tab-view")
  public Text remove(
      Channel channel,
      @Required(name = "player", description = "The player to remove") Player player) {
    channel.getTabView().remove(new PlayerTabEntry(player));
    return Text.of("Player removed");
  }

  @CommandPermission("babel.tab")
  @Command(aliases = "set", description = "Set a text tab-view entry")
  public Text set(
      Channel channel,
      @Required(name = "x", description = "The x coordinate of the entry") int x,
      @Required(name = "y", description = "The y coordinate of the entry") int y,
      @Required(name = "text", description = "The text of the entry to display") Text text) {
    channel.getTabView().set(new TabCoordinate(x, y), new TextTabEntry(text));
    return Text.of("Entry set");
  }

  @CommandPermission("babel.tab")
  @Command(aliases = "sort", description = "Sort the tab-view")
  public Text sort(Channel channel) {
    channel.getTabView().sort();
    return Text.of("Tab view sorted");
  }
}
