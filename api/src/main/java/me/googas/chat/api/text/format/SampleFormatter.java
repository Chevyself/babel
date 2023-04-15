package me.googas.chat.api.text.format;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.text.Text;
import me.googas.chat.api.text.Localized;

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
  public @NonNull Text format(@NonNull Text text) {
    Locale locale;
    if (text instanceof Localized) {
      locale = ((Localized) text).getLocale();
    } else {
      locale = ResourceManager.getBase();
    }
    return this.format(locale, text);
  }

  @Override
  public @NonNull Text format(@NonNull Locale locale, @NonNull Text text) {
    String raw = text.getRaw();
    Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
    while (matcher.find()) {
      String group = matcher.group();
      // I don't really understand why remove quotation marks
      // keep it removed until theres a reason
      // String key = group.replace("\"", "");
      String key = group.substring(2, group.length() - 1);
      raw = raw.replace(group, Text.localized(locale, key).getRaw());
    }
    text.getExtra().forEach(child -> this.format(locale, child));
    return text.setRaw(raw);
  }
}
