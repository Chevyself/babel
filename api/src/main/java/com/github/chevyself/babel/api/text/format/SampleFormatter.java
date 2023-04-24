package com.github.chevyself.babel.api.text.format;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.text.LocalizedText;
import com.github.chevyself.babel.api.text.Text;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

/** Formatter for sample lines. */
public final class SampleFormatter implements Formatter, LocalizedText.LocalizedFormatter {

  /**
   * Pattern to find anything that is inside a word starting with '$' and inside '{}' such as:
   *
   * <p>${this.will.be.matched}
   */
  @NonNull private static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)}");

  /** Creates a new instance. */
  public SampleFormatter() {}

  @Override
  public @NonNull Text format(@NonNull Text text) {
    Locale locale;
    if (text instanceof LocalizedText) {
      locale = ((LocalizedText) text).getLocale();
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
      // keep it removed until there's a reason
      // String key = group.replace("\"", "");
      String key = group.substring(2, group.length() - 1);
      raw = raw.replace(group, Text.localized(locale, key).getRaw());
    }
    text.getExtra().forEach(child -> this.format(locale, child));
    return text.setRaw(raw);
  }
}
