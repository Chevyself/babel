package me.googas.chat.api.commands;

import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;
import me.googas.chat.api.channels.Channel;

public final class ChannelProvider implements BukkitExtraArgumentProvider<Channel> {
  @Override
  public @NonNull Channel getObject(@NonNull CommandContext commandContext) {
    return Channel.of(commandContext.getSender());
  }

  @Override
  public @NonNull Class<Channel> getClazz() {
    return Channel.class;
  }
}
