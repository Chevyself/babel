package com.github.chevyself.babel.api.commands;

import com.github.chevyself.babel.api.text.Text;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

/** Implementation of {@link BukkitArgumentProvider} that provides {@link Text} for commands. */
public class TextProvider implements BukkitArgumentProvider<Text> {

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return new ArrayList<>();
  }

  @Override
  public @NonNull Text fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    return Text.parse(string);
  }

  @Override
  public @NonNull Class<Text> getClazz() {
    return Text.class;
  }
}
