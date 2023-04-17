package com.github.chevyself.babel.api.commands;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.result.BukkitResult;
import chevyself.github.commands.bukkit.utils.BukkitUtils;
import chevyself.github.commands.result.StarboxResult;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.text.Text;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

public final class ResultHandler implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    CommandSender sender = context.getSender();
    Channel channel = Channel.of(sender);
    if (result instanceof Text) {
      ((Text) result).setSample(true).send(channel);
    } else if (result instanceof BukkitResult) {
      BukkitUtils.send(sender, ((BukkitResult) result).getComponents());
    }
  }
}
