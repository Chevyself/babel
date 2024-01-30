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
   * Pattern to match keys in the sample lines.
   *
   * <ul>
   *   <li>To match keys with whitespaces or any use ${key with whitespaces}
   *   <li>To match keys without whitespaces use $keyWithoutWhitespaces
   * </ul>
   */
  @NonNull
  private static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)}|\\$([^\\s\"\\]})]+)");

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
      String key = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
      raw = raw.replace(group, Text.localized(locale, key).getRaw());
    }
    text.getExtra().forEach(child -> this.format(locale, child));
    return text.setRaw(raw);
  }
}
