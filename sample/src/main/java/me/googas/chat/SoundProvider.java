package me.googas.chat;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import me.googas.chat.api.text.Text;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import org.bukkit.Sound;

public class SoundProvider implements BukkitArgumentProvider<Sound> {

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
      throw Text.localized(context.getSender(), "error.not-sound").asProviderException();
    }
  }

  @Override
  public @NonNull Class<Sound> getClazz() {
    return Sound.class;
  }
}
