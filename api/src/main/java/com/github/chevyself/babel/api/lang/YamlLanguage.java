package com.github.chevyself.babel.api.lang;

import com.github.chevyself.babel.api.exceptions.LanguageParsingException;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.util.Strings;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/** Represents a language inside a yaml file. */
public final class YamlLanguage implements Language {

  @NonNull @Getter private final Locale locale;
  @NonNull private final YamlConfiguration configuration;
  @Getter private final boolean sample;

  private YamlLanguage(
      @NonNull Locale locale, @NonNull YamlConfiguration configuration, boolean sample) {
    this.locale = locale;
    this.configuration = configuration;
    this.sample = sample;
  }

  /**
   * Get the language from a configuration.
   *
   * <p>YAML Languages must have the field 'language' that must match a {@link Locale}, or sample in case
   * the language is a sample. For instance:
   *
   * <pre>
   *   language: en # english
   *   language: sample # sample language
   *   language: en-US # english (United States)
   *   language: es-ES # spanish (Spain)
   *   language: pt-BR # portuguese (Brazil)
   *
   * @param configuration the configuration to get the language from
   * @return the language
   * @throws LanguageParsingException if the language cannot be parsed
   */
  @NonNull
  public static YamlLanguage of(@NonNull YamlConfiguration configuration)
      throws LanguageParsingException {
    String language = configuration.getString("language");
    if (language != null) {
      boolean sample = language.equalsIgnoreCase("sample");
      return new YamlLanguage(new Locale(sample ? "en" : language), configuration, sample);
    } else {
      throw new LanguageParsingException(
          "YamlConfiguration is missing the field 'language' @ " + configuration);
    }
  }

  /**
   * Get the language from a resource and save it to a file.
   *
   * @see #of(YamlConfiguration)
   * @param resource the resource to get the language from
   * @param file the file to save the language to
   * @return the language
   * @throws LanguageParsingException if the language cannot be parsed
   */
  @NonNull
  public static YamlLanguage load(@NonNull InputStream resource, @NonNull File file)
      throws LanguageParsingException {
    try (Reader isReader = new InputStreamReader(resource);
        Reader fileReader = new FileReader(file)) {
      YamlConfiguration resourceYml = YamlConfiguration.loadConfiguration(isReader);
      YamlConfiguration fileYml = YamlConfiguration.loadConfiguration(fileReader);
      fileYml.addDefaults(resourceYml);
      fileYml.options().copyDefaults(true);
      fileYml.save(file);
      return YamlLanguage.of(fileYml);
    } catch (IOException e) {
      throw new LanguageParsingException(
          "Failed to handle reader either from resource or file in " + file);
    }
  }

  /**
   * Load a list of languages from a plugin's resource directory and save them to a file. The
   * languages will be saved if they do not exist. The resource path is just the name of the
   * resource, if it does not have the extension '.yml', it will be added.
   *
   * @param plugin the plugin to get the resources from
   * @param directory the directory to save the languages to
   * @param lang the languages to load, this must match the resource path
   * @return the languages
   * @throws IOException if the directory cannot be created
   */
  @NonNull
  public static List<YamlLanguage> load(
      @NonNull Plugin plugin, @NonNull File directory, @NonNull String... lang) throws IOException {
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Could not create directory" + directory);
    } else {
      List<YamlLanguage> languages = new ArrayList<>();
      for (String name : lang) {
        String resourcePath = !name.endsWith(".yml") ? name + ".yml" : name;
        InputStream resource = plugin.getResource(resourcePath);
        if (resource != null) {
          File file = new File(directory, resourcePath.replace("/", File.separator));
          try {
            if (!file.exists()
                && (file.getParentFile().exists()
                    ? !file.createNewFile()
                    : !file.getParentFile().mkdirs() || !file.createNewFile())) {
              Debugger.getInstance().getLogger().log(Level.SEVERE, "Could not create file " + file);
            } else {
              languages.add(YamlLanguage.load(resource, file));
            }
          } catch (LanguageParsingException e) {
            Debugger.getInstance()
                .getLogger()
                .log(Level.SEVERE, "Could not parse language in " + file, e);
          } catch (IOException e) {
            Debugger.getInstance().getLogger().log(Level.SEVERE, "Failed to load " + file, e);
          }
        } else {
          Debugger.getInstance().getLogger().severe("Could not find resource " + resourcePath);
        }
      }
      return languages;
    }
  }

  @NonNull
  public Optional<String> getRaw(@NonNull String key) {
    return Optional.ofNullable(configuration.getString(key));
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key) {
    return Components.getComponent(Strings.format(this.getRaw(key).orElse(key).trim()));
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key, @NonNull Map<String, String> map) {
    return Components.getComponent(Strings.format(this.getRaw(key).orElse(key), map).trim());
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key, Object... objects) {
    return Components.getComponent(Strings.format(this.getRaw(key).orElse(key), objects).trim());
  }
}
