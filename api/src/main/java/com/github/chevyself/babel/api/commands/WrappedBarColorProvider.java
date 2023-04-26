package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.packet.bossbar.WrappedBarColor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * Implementation of {@link BukkitArgumentProvider} that provides {@link WrappedBarColor} for
 * commands.
 */
public class WrappedBarColorProvider implements BukkitArgumentProvider<WrappedBarColor> {

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return Arrays.stream(WrappedBarColor.values())
        .map(color -> color.toString().toLowerCase())
        .collect(Collectors.toList());
  }

  @Override
  public @NonNull WrappedBarColor fromString(
      @NonNull String string, @NonNull CommandContext context) throws ArgumentProviderException {
    try {
      return WrappedBarColor.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("Invalid color: " + string);
    }
  }

  @Override
  public @NonNull Class<WrappedBarColor> getClazz() {
    return WrappedBarColor.class;
  }
}
