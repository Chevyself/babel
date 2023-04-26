package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.packet.bossbar.WrappedBarStyle;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.Sound;

/** Implementation of {@link WrappedBarStyle} that provides {@link Sound} for commands. */
public class WrappedBarStyleProvider implements BukkitArgumentProvider<WrappedBarStyle> {

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return Arrays.stream(WrappedBarStyle.values())
        .map(style -> style.toString().toLowerCase())
        .collect(Collectors.toList());
  }

  @Override
  public @NonNull WrappedBarStyle fromString(
      @NonNull String string, @NonNull CommandContext context) throws ArgumentProviderException {
    try {
      return WrappedBarStyle.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("Invalid bar style: " + string);
    }
  }

  @Override
  public @NonNull Class<WrappedBarStyle> getClazz() {
    return WrappedBarStyle.class;
  }
}
