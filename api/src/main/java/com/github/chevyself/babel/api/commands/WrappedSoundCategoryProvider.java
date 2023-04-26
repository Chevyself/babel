package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.Sound;

/** Implementation of {@link WrappedSoundCategory} that provides {@link Sound} for commands. */
public class WrappedSoundCategoryProvider implements BukkitArgumentProvider<WrappedSoundCategory> {

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return Arrays.stream(WrappedSoundCategory.values())
        .map(category -> category.name().toLowerCase())
        .map(String::toLowerCase)
        .collect(Collectors.toList());
  }

  @Override
  public @NonNull WrappedSoundCategory fromString(
      @NonNull String string, @NonNull CommandContext context) throws ArgumentProviderException {
    try {
      return WrappedSoundCategory.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("Invalid sound category: " + string);
    }
  }

  @Override
  public @NonNull Class<WrappedSoundCategory> getClazz() {
    return WrappedSoundCategory.class;
  }
}
