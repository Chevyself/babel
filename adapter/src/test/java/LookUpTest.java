import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.babel.util.Versions.BukkitVersion;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LookUpTest {

  public static void main(String[] args) {
    Random random = new Random();
    LookUp<Object, WrappedField<Object>> lookUp = LookUp.fieldOn(WrappedClass.of(Versions.class))
        .since(8, "playerVersions")
        .since(16, "c");
    Set<Integer> versions = new HashSet<>();
    while (versions.size() < 19 - 8) {
      // Random int from 8 to 19
      int version = random.nextInt(19 - 8 + 1) + 8;
      String name = lookUp.getName(new BukkitVersion(version, 0));
      System.out.println("Version " + version + " has field name " + name);
      versions.add(version);
    }
  }

}
