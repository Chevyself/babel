package com.github.chevyself.babel.util;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

/** Utility class for Bukkit versioning. */
public final class Versions {
  @NonNull
  private static final List<PlayerVersion> PLAYER_VERSION_VERSIONS =
      Arrays.asList(
          new PlayerVersion(7, 0, 10),
          new PlayerVersion(8, 47, 47),
          new PlayerVersion(9, 107, 110),
          new PlayerVersion(10, 210, 210),
          new PlayerVersion(11, 315, 316),
          new PlayerVersion(12, 335, 340),
          new PlayerVersion(13, 393, 404),
          new PlayerVersion(14, 477, 498),
          new PlayerVersion(15, 573, 578),
          new PlayerVersion(16, 735, 754),
          new PlayerVersion(17, 755, 756),
          new PlayerVersion(18, 757, 758),
          new PlayerVersion(19, 759, 759));

  public static final int MIN_BUKKIT = 8;
  public static final int MAX_BUKKIT = 19;
  @NonNull private static final BukkitVersion BUKKIT_VERSION = Versions.checkBukkit();

  /**
   * Bukkit's major version.
   *
   * @deprecated use {@link #getBukkit()} instead.
   */
  @Deprecated public static final int BUKKIT = Versions.check();

  @NonNull
  private static BukkitVersion checkBukkit() {
    String versionString = Bukkit.getBukkitVersion();
    // Bukkit version is x.x.x-Rx Then we must split from '-' first
    String[] parts = versionString.split("-");
    // Now we can get the numbers
    String[] numbers = parts[0].split("\\.");
    // 0 is ignored because its always 1
    int major = Integer.parseInt(numbers[1]);
    int minor = numbers.length > 2 ? Integer.parseInt(numbers[2]) : 0;
    return new BukkitVersion(major, minor);
  }

  /**
   * Checks what's the current server's Bukkit version.
   *
   * @return the Bukkit version
   */
  @Deprecated
  public static int check() {
    return Versions.BUKKIT_VERSION.getMajor();
  }

  /**
   * Get the version of a player based on its protocol number.
   *
   * @param protocol the protocol number to get the version
   * @return the version of the player
   */
  public static PlayerVersion getVersion(int protocol) {
    return Versions.PLAYER_VERSION_VERSIONS.stream()
        .filter(
            playerVersion ->
                protocol >= playerVersion.getMin() && protocol <= playerVersion.getMax())
        .findFirst()
        .orElseGet(Versions::getProtocol);
  }

  /**
   * Get the player version of the current 'Bukkit' version.
   *
   * @return the player version
   */
  public static PlayerVersion getProtocol() {
    return Versions.PLAYER_VERSION_VERSIONS.stream()
        .filter(playerVersion -> playerVersion.getBukkit() == Versions.BUKKIT)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  /**
   * Get the canonical name of a class in the NMS package.
   *
   * @see #getCanonicalName(String, String)
   * @param className the class name
   * @return the canonical name of the class
   */
  @NonNull
  public static String getCanonicalName(@NonNull String className) {
    return Versions.getCanonicalName(null, className);
  }

  /**
   * Get the canonical name of a class in the NMS package. This is used to get the class name of a
   * NMS
   *
   * <p>In 1.17 and above, the NMS package is now 'net.minecraft.(added).*'. In 1.16 and below, the
   * NMS package is 'net.minecraft.server.(version).*'.
   *
   * <p>If the Bukkit version is above or equal to 1.17, and the added package is not null, the
   * added package will default to 'network.protocol.game'. As it contains Packet classes
   *
   * @param addedPackage the package to add to the NMS package which is not used in 1.16 and below,
   *     thus this can be null
   * @param className the class name
   * @return the canonical name of the class
   */
  @NonNull
  public static String getCanonicalName(@Nullable String addedPackage, @NonNull String className) {
    if (Versions.BUKKIT >= 17) {
      return "net.minecraft."
          + (addedPackage == null ? "network.protocol.game." : (addedPackage + "."))
          + className;
    } else {
      return "net.minecraft.server." + Packet.NMS + "." + className;
    }
  }

  /**
   * Wrap a NMS class by its name.
   *
   * @see #wrapNmsClassByName(String, String)
   * @param name the name of the class
   * @return the wrapped class
   */
  @NonNull
  public static WrappedClass<?> wrapNmsClassByName(@NonNull String name) {
    return Versions.wrapNmsClassByName(null, name);
  }

  /**
   * Wrap a NMS class by its name.
   *
   * <p>This will get the canonical name of the class and wrap it.
   *
   * @see #getCanonicalName(String, String)
   * @param addedPackage the package that contains the class
   * @param name the name of the class
   * @return the wrapped class
   */
  @NonNull
  public static WrappedClass<?> wrapNmsClassByName(String addedPackage, @NonNull String name) {
    return WrappedClass.forName(Versions.getCanonicalName(addedPackage, name));
  }

  /**
   * Get the current Bukkit version.
   *
   * @return the Bukkit version
   */
  public static BukkitVersion getBukkit() {
    return Versions.BUKKIT_VERSION;
  }

  /**
   * Represents a Bukkit version. This is used to compare versions and check if a version applies to
   * another.
   */
  public static class BukkitVersion implements Comparable<BukkitVersion> {

    @Getter private final int major;
    @Getter private final int minor;

    /**
     * Creates a new Bukkit version.
     *
     * <p>The major version is the second number of the version. The minor version is the third
     * number
     *
     * <p>For instance: 1.8.9 is major 8 and minor 9, 1.16.5 is major 16 and minor 5, 1.7 is major 7
     * and minor 0.
     *
     * @param major the major version
     * @param minor the minor version
     */
    public BukkitVersion(int major, int minor) {
      this.major = major;
      this.minor = minor;
    }

    /**
     * Checks if this version applies to another.
     *
     * <p>For instance 1.10 applies to 1.8 but 1.10 does not apply to 1.11
     *
     * @param other the other version
     * @return true if this version applies to the other
     */
    public boolean applies(@NonNull BukkitVersion other) {
      return this.major >= other.major && (this.major > other.major || (this.minor <= other.minor));
    }

    @Override
    public int compareTo(@NonNull Versions.BukkitVersion o) {
      return this.major == o.major
          ? Integer.compare(this.minor, o.minor)
          : Integer.compare(this.major, o.major);
    }

    @Override
    public String toString() {
      return "1." + major + "." + minor;
    }

    public boolean isPrevious(int major, int minor) {
      return this.isPrevious(new BukkitVersion(major, minor));
    }

    /**
     * Checks if this version is previous to another.
     *
     * <p>For instance 1.10 is previous to 1.11 but 1.10 is not previous to 1.9
     * <p>If both versions are equal, this will return false
     *
     * @param other the other version
     * @return true if this version is previous to the other
     */
    public boolean isPrevious(@NonNull BukkitVersion other) {
      return this.major < other.major || this.major == other.major
          && this.minor < other.minor;
    }

    public boolean isNext(int major, int minor) {
      return this.isNext(new BukkitVersion(major, minor));
    }

    /**
     * Checks if this version is next to another.
     *
     * <p>For instance 1.10 is next to 1.9 but 1.10 is not next to 1.11
     * <p>If both versions are equal, this will return false
     *
     * @param other the other version
     * @return true if this version is next to the other
     */
    public boolean isNext(@NonNull BukkitVersion other) {
      return this.major > other.major || this.major == other.major
          && this.minor > other.minor;
    }
  }

  /**
   * Represents the version of a {@link org.bukkit.entity.Player}. This includes the 'Bukkit'
   * version with matches with {@link #BUKKIT} and the minimum and maximum protocol number
   */
  public static class PlayerVersion {

    @Getter private final int bukkit;
    @Getter private final int min;
    @Getter private final int max;

    private PlayerVersion(int bukkit, int min, int max) {
      this.bukkit = bukkit;
      this.min = min;
      this.max = max;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", PlayerVersion.class.getSimpleName() + "[", "]")
          .add("bukkit=" + bukkit)
          .add("min=" + min)
          .add("max=" + max)
          .toString();
    }
  }
}
