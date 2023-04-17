package com.github.chevyself.babel.api.text;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.api.placeholders.PlaceholderManager;
import com.github.chevyself.babel.api.text.format.SampleFormatter;
import java.util.Locale;
import lombok.NonNull;

final class TextUtils {

  /**
   * Extracts the key from a string. If the prefix is '$', it will remove the prefix and return the
   * rest of the string, otherwise it will remove the prefix 'localized' and the colon.
   *
   * @param string the string to extract the key from
   * @return the extracted key
   * @throws NullPointerException if the string is null
   */
  @NonNull
  static String extractKey(@NonNull String string) {
    String prefix = string.startsWith("localized:") ? "localized:" : "$";
    if (prefix.equals("$")) {
      if (string.startsWith("${") && string.endsWith("}")) {
        return string.substring(2, string.length() - 1);
      } else {
        return string.substring(1);
      }
    } else {
      return string.substring(10);
    }
  }

  /**
   * Checks whether a string is localized text. A string is localized text if it starts with
   * 'localized:' or '$' and it does not contain any spaces.
   *
   * @param string the string to check
   * @return true if the string is localized text
   * @throws NullPointerException if the string is null
   */
  static boolean isLocalized(@NonNull String string) {
    return !string.contains(" ") && (string.startsWith("localized:") || string.startsWith("$"));
  }

  /**
   * Checks if a string is a json.
   *
   * @param string the string to check
   * @return true if the string is a json
   */
  static boolean isJson(@NonNull String string) {
    return string.startsWith("{") && string.endsWith("}")
        || string.startsWith("[") && string.endsWith("]");
  }

  /**
   * Formats the sample in the line using the {@link SampleFormatter}.
   *
   * @param channel the channel to build the sample for
   * @param copy the line to format
   * @throws NullPointerException if the channel or line is null
   */
  static void formatSample(@NonNull Locale channel, @NonNull Text copy) {
    ResourceManager.getInstance().getSampleFormatter().format(channel, copy);
  }

  /**
   * Formats the placeholders in the line using the {@link PlaceholderManager}.
   *
   * @param channel the channel to build the placeholders for
   * @param copy the line to format
   * @throws NullPointerException if the channel or line is null
   */
  static void formatPlaceholders(@NonNull Channel channel, @NonNull Text copy) {
    copy.setRaw(PlaceholderManager.getInstance().build(channel, copy.getRaw()));
  }
}
