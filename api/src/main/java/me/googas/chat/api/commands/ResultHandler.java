package me.googas.chat.api.commands;

import lombok.NonNull;
import me.googas.chat.api.channels.Channel;
import me.googas.chat.api.text.Text;
import me.googas.commands.Middleware;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.result.StarboxResult;
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
