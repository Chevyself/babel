package com.github.chevyself.babel.commands;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.bukkit.annotations.Command;

public class ChannelCommands {

  @Command(
      aliases = {"headerFooter", "hF"},
      description = "Set the header and footer of the tab-list",
      permission = "babel.channel")
  public Text headerFooter(
      Channel channel,
      @Required(
              name = "header",
              description = "The text of the header",
              behaviour = ArgumentBehaviour.MULTIPLE)
          Text header,
      @Required(
              name = "footer",
              description = "The text of the footer",
              behaviour = ArgumentBehaviour.MULTIPLE)
          Text footer) {
    channel.setTabList(header, footer);
    return Text.localized("cmd.header-footer")
        .placeholder("header", header.asString(channel))
        .placeholder("footer", footer.asString(channel));
  }

  @Command(
      aliases = {"title"},
      description = "Send a title to the player",
      permission = "babel.channel")
  public Text title(
      Channel channel,
      @Required(
              name = "title",
              description = "The title to show",
              behaviour = ArgumentBehaviour.MULTIPLE)
          Text title,
      @Required(
              name = "subtitle",
              description = "The subtitle to show",
              behaviour = ArgumentBehaviour.MULTIPLE)
          Text subtitle,
      @Free(
              name = "fade in",
              description = "The time that the title has to fade in",
              suggestions = "20")
          int fadeIn,
      @Free(name = "stay", description = "The time that the title will stay", suggestions = "40")
          int stay,
      @Free(
              name = "fade out",
              description = "The time that the title will fade out",
              suggestions = "20")
          int fadeOut) {
    channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    return Text.localized("cmd.title")
        .placeholder("title", title.asString(channel))
        .placeholder("subtitle", subtitle.asString(channel))
        .placeholder("fade-in", fadeIn)
        .placeholder("stay", stay)
        .placeholder("fade-out", fadeOut);
  }
}
