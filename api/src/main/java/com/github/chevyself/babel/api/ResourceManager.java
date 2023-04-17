package com.github.chevyself.babel.api;

import com.github.chevyself.babel.api.lang.Language;
import com.github.chevyself.babel.api.text.format.SampleFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

public final class ResourceManager {

  @NonNull @Getter @Setter private static Locale base = Locale.ENGLISH;
  @NonNull @Getter private static final ResourceManager instance = new ResourceManager();

  @NonNull @Getter private final SampleFormatter sampleFormatter = new SampleFormatter();
  @NonNull private final Map<Plugin, List<Language>> languages = new HashMap<>();

  /**
   * Register a {@link Language}.
   *
   * @param plugin the plugin that is registering the language
   * @param language the language that is being registered
   * @return this same instance
   */
  public ResourceManager register(@NonNull Plugin plugin, @NonNull Language language) {
    this.languages.computeIfAbsent(plugin, pluginKey -> new ArrayList<>()).add(language);
    return this;
  }

  /**
   * Register many {@link Language}.
   *
   * @param plugin the plugin that is registering the languages
   * @param languages the language that is being registered
   * @return this same instance
   */
  public ResourceManager registerAll(
      @NonNull Plugin plugin, @NonNull Collection<? extends Language> languages) {
    this.languages.computeIfAbsent(plugin, pluginKey -> new ArrayList<>()).addAll(languages);
    return this;
  }

  /**
   * Unregisters all languages from a {@link Plugin}.
   *
   * @param plugin the plugin to unregister the languages
   * @return this same instance
   */
  public ResourceManager unregister(@NonNull Plugin plugin) {
    this.languages.remove(plugin);
    return this;
  }

  /**
   * Get a bukkit languages from a locale.
   *
   * @param locale the locale to get the bukkit languages for
   * @return the list of languages
   */
  public List<Language> getLanguages(@NonNull Locale locale) {
    return this.languages.values().stream()
        .flatMap(Collection::stream)
        .filter(
            language ->
                language.isSample()
                    || language.getLocale().getLanguage().equalsIgnoreCase(locale.getLanguage()))
        .collect(Collectors.toList());
  }

  /**
   * Get the raw message from a key. If no message is found for the 'key' the same 'key' will be
   * returned
   *
   * @param locale the locale to get the language
   * @param key the key to get the raw message
   * @return the raw message
   */
  @NonNull
  public String getRaw(@NonNull Locale locale, @NonNull String key) {
    return this.getLanguages(locale).stream()
        .map(language -> language.getRaw(key).orElse(key))
        .filter(raw -> !raw.equals(key))
        .findFirst()
        .orElseGet(
            () -> {
              if (locale.getLanguage().equals("en")) {
                return key;
              } else {
                return this.getRaw(ResourceManager.getBase(), key);
              }
            });
  }
}
