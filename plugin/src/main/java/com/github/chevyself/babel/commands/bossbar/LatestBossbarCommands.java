package com.github.chevyself.babel.commands.bossbar;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.bukkit.annotations.Command;

public class LatestBossbarCommands {

  @Command(
      aliases = "color",
      description = "Set the color of the bossbar",
      permission = "babel.bossbar.color")
  public Text color(
      Channel channel,
      @Required(name = "color", description = "The color of the bossbar") WrappedBarColor color) {
    channel.getBossBar().setColor(color);
    return Text.of("Bossbar color set");
  }

  @Command(
      aliases = "style",
      description = "Set the style of the bossbar",
      permission = "babel.bossbar.style")
  public Text style(
      Channel channel,
      @Required(name = "style", description = "The style of the bossbar") WrappedBarStyle style) {
    channel.getBossBar().setStyle(style);
    return Text.of("Bossbar style set");
  }
}
