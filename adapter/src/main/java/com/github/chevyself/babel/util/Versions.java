package com.github.chevyself.babel.util;

import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

/** Utility class for Bukkit versioning. */
public final class Versions {
  @NonNull
  private static final List<Player> playerVersions =
      Arrays.asList(
          new Player(7, 0, 10),
          new Player(8, 47, 47),
          new Player(9, 107, 110),
          new Player(10, 210, 210),
          new Player(11, 315, 316),
          new Player(12, 335, 340),
          new Player(13, 393, 404),
          new Player(14, 477, 498),
          new Player(15, 573, 578),
          new Player(16, 735, 754),
          new Player(17, 755, 756),
          new Player(18, 757, 758),
          new Player(19, 759, 759));

  public static final int MIN_BUKKIT = 8;
  public static final int MAX_BUKKIT = 19;
  @NonNull private static BukkitVersion BUKKIT_VERSION = Versions.checkBukkit();

  public static int BUKKIT = Versions.check();

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
  public static Player getVersion(int protocol) {
    return Versions.playerVersions.stream()
        .filter(player -> protocol >= player.getMin() && protocol <= player.getMax())
        .findFirst()
        .orElseGet(Versions::getProtocol);
  }

  /**
   * Get the player version of the current 'Bukkit' version.
   *
   * @return the player version
   */
  public static Player getProtocol() {
    return Versions.playerVersions.stream()
        .filter(player -> player.getBukkit() == Versions.BUKKIT)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  @NonNull
  public static String getCanonicalName(@NonNull String className) {
    return Versions.getCanonicalName(null, className);
  }

  @NonNull
  public static String getCanonicalName(String addedPackage, @NonNull String className) {
    if (Versions.BUKKIT >= 17) {
      return "net.minecraft."
          + (addedPackage == null ? "network.protocol.game." : (addedPackage + "."))
          + className;
    } else {
      return "net.minecraft.server." + Packet.NMS + "." + addedPackage + "." + className;
    }
  }

  @NonNull
  public static WrappedClass<?> wrapNmsClassByName(@NonNull String name) {
    return Versions.wrapNmsClassByName(null, name);
  }

  @NonNull
  public static WrappedClass<?> wrapNmsClassByName(String addedPackage, @NonNull String name) {
    return WrappedClass.forName(Versions.getCanonicalName(addedPackage, name));
  }

  public static BukkitVersion getBukkit() {
    return BUKKIT_VERSION;
  }

  public static class BukkitVersion implements Comparable<BukkitVersion> {

    @Getter private final int major;
    @Getter private final int minor;

    public BukkitVersion(int major, int minor) {
      this.major = major;
      this.minor = minor;
    }

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
  }

  /**
   * Represents the version of a {@link org.bukkit.entity.Player}. This includes the 'Bukkit'
   * version with matches with {@link #BUKKIT} and the minimum and maximum protocol number
   */
  public static class Player {

    @Getter private final int bukkit;
    @Getter private final int min;
    @Getter private final int max;

    private Player(int bukkit, int min, int max) {
      this.bukkit = bukkit;
      this.min = min;
      this.max = max;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
          .add("bukkit=" + bukkit)
          .add("min=" + min)
          .add("max=" + max)
          .toString();
    }
  }
}
