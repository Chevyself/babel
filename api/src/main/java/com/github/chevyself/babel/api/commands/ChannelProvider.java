package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;

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
