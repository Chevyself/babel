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

public final class ResultHandler implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    Channel channel = Channel.of(context.getSender());
    if (result instanceof LocalizedReference) {
      channel.send(((LocalizedReference) result).asLocalized(channel));
    } else if (result instanceof Line) {
      channel.send(((Line) result));
    } else if (result instanceof BukkitResult) {
      BukkitUtils.send(context.getSender(), ((BukkitResult) result).getComponents());
    }
  }
}
