package me.googas.chat.api.lines.format;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.Localized;
import me.googas.chat.api.lines.LocalizedReference;

/** Formatter for sample lines. */
public final class SampleFormatter implements Formatter, Localized.LocalizedFormatter {

  @NonNull private static final Pattern PATTERN = Pattern.compile("(?<!\\w)\\$\\w(\\S+)");

  public SampleFormatter() {}

  @Override
  public @NonNull Line format(@NonNull Line line) {
    Locale locale;
    if (line instanceof Localized) {
      locale = ((Localized) line).getLocale();
    } else {
      locale = Locale.ENGLISH;
    }
    return this.format(locale, line);
  }

  @Override
  public @NonNull Line format(@NonNull Locale locale, @NonNull Line line) {
    String raw = line.getRaw();
    Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
    while (matcher.find()) {
      String key = matcher.group().replace("\"", "");
      Line bukkitLine = Line.parse(key);
      String replacement;
      if (bukkitLine instanceof LocalizedReference) {
        LocalizedReference reference = (LocalizedReference) bukkitLine;
        replacement = reference.asLocalized(locale).getRaw();
      } else {
        replacement = bukkitLine.getRaw();
      }
      raw = raw.replace(key, replacement);
    }
    return line.setRaw(raw);
  }
}
