import com.github.chevyself.babel.util.Versions.BukkitVersion;

public class VersionsTest {

  public static void main(String[] args) {
    BukkitVersion version = new BukkitVersion(15, 1);
    System.out.println(version.isPrevious(7, 0));
    System.out.println(version.isPrevious(15, 0));
    System.out.println(version.isPrevious(15, 1));
    System.out.println(version.isNext(15, 2));
    System.out.println(version.isNext(16, 0));
    System.out.println(version.isNext(19, 0));
    System.out.println("----");
    System.out.println(version.isNext(7, 0));
    System.out.println(version.isNext(15, 0));
    System.out.println(version.isNext(15, 1));
    System.out.println(version.isPrevious(15, 2));
    System.out.println(version.isPrevious(16, 0));
    System.out.println(version.isPrevious(19, 0));
  }
}
