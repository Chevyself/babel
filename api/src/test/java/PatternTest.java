import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

public class PatternTest {

  @NonNull
  private static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)}|\\$([^\\s\"\\[\\]{}()]+)");

  public static void main(String[] args) {
    String subject =
        "Testing keys:\n"
            + "\n"
            + "  ${this should be matched by group 1}\n"
            + "  $thisShouldBeMatchedByGroup2\n"
            + "  $thisShouldStopHere]andNotContinue\n"
            + "  $thisShouldStopHere\"andNotContinue\n"
            + "  $thisShouldStopHere)andNotContinue";

    Matcher matcher = PATTERN.matcher(subject);
    while (matcher.find()) {
      System.out.println(matcher.groupCount());
      System.out.println("matcher.group() = " + matcher.group());
      System.out.println("matcher.group(1) = " + matcher.group(1));
      System.out.println("matcher.group(2) = " + matcher.group(2));
    }
  }
}
