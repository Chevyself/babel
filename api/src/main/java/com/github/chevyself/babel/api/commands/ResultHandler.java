package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.Result;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

/** Implementation of {@link Middleware} that handles the display of the result from a command. */
public final class ResultHandler implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    CommandSender sender = context.getSender();
    Channel channel = Channel.of(sender);
    if (result instanceof Text) {
      ((Text) result).send(channel);
    }
  }
}
