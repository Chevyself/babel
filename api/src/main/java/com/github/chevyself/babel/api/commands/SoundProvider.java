package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.bukkit.Sound;

/** Implementation of {@link BukkitArgumentProvider} that provides {@link Sound} for commands. */
public final class SoundProvider implements BukkitArgumentProvider<Sound> {

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return Stream.of(Sound.values())
        .map(sound -> sound.toString().toLowerCase(Locale.ROOT))
        .collect(Collectors.toList());
  }

  @Override
  public @NonNull Sound fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    try {
      return Sound.valueOf(string.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw Text.localized(context.getSender(), "error.not-sound")
          .format(string)
          .asProviderException();
    }
  }

  @Override
  public @NonNull Class<Sound> getClazz() {
    return Sound.class;
  }
}
