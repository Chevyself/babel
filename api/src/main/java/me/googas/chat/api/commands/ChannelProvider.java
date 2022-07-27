package me.googas.chat.api.commands;

import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitExtraArgumentProvider;

public class ChannelProvider implements BukkitExtraArgumentProvider<Channel> {
  @Override
  public @NonNull Channel getObject(@NonNull CommandContext commandContext) {
    return Channel.of(commandContext.getSender());
  }

  @Override
  public @NonNull Class<Channel> getClazz() {
    return Channel.class;
  }
}
