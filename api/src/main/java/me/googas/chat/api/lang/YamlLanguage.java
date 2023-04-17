package me.googas.chat.api.lang;

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
import me.googas.chat.api.exceptions.LanguageParsingException;
import me.googas.chat.debug.Debugger;
import me.googas.commands.bukkit.utils.Components;
import me.googas.commands.util.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

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
              Debugger.getInstance().handle(Level.SEVERE, "Could not create file " + file);
            } else {
              languages.add(YamlLanguage.load(resource, file));
            }
          } catch (LanguageParsingException e) {
            Debugger.getInstance().handle(Level.SEVERE, "Could not parse language in " + file, e);
          } catch (IOException e) {
            Debugger.getInstance().handle(Level.SEVERE, "Failed to create file in " + file, e);
          }
        } else {
          Debugger.getInstance().handle(Level.SEVERE, "Could not find resource " + resourcePath);
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
