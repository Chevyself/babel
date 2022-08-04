package me.googas.chat.api.lines.format;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.chat.api.lines.Line;
import me.googas.chat.api.lines.Localized;

/** Formatter for sample lines. */
public final class SampleFormatter implements Formatter, Localized.LocalizedFormatter {

  /**
   * Pattern to find anything that is inside a word starting with '$' and inside '{}' such as:
   *
   * <p>${this.will.be.matched}
   */
  @NonNull private static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)}");

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
      String group = matcher.group();
      // I don't really understand why remove quotation marks
      // keep it removed until theres a reason
      // String key = group.replace("\"", "");
      String key = group.substring(2, group.length() - 1);
      raw = raw.replace(group, Line.localized(locale, key).getRaw());
    }
    line.getExtra().forEach(child -> this.format(locale, child));
    return line.setRaw(raw);
  }
}
