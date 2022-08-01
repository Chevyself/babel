package me.googas.chat.api.commands;

import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.LocalizedReference;
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
    if (result instanceof LocalizedReference) {
      channel.send(((LocalizedReference) result).asLocalized(channel).formatSample());
    } else if (result instanceof Line) {
      channel.send(((Line) result).formatSample());
    } else if (result instanceof BukkitResult) {
      BukkitUtils.send(sender, ((BukkitResult) result).getComponents());
    }
  }
}
