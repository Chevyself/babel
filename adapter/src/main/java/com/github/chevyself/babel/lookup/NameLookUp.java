package com.github.chevyself.babel.lookup;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.util.ReflectUtil;
import java.util.TreeMap;
import lombok.NonNull;

/**
 * Represents a look-up which provides a way to provide names across different versions of Bukkit.
 *
 * @param <T>
 */
public abstract class NameLookUp<T> implements LookUp<T> {
  @NonNull protected final TreeMap<Versions.BukkitVersion, String> nameMap = new TreeMap<>();

  /**
   * Get the name of the field or method in the given version of Bukkit.
   *
   * @param bukkit the version of Bukkit
   * @return the name
   */
  @NonNull
  public String getName(Versions.BukkitVersion bukkit) {
    return ReflectUtil.nonNull(
            this.nameMap.floorEntry(bukkit),
            new NullPointerException("Could not match a name for " + this + " in " + bukkit))
        .getValue();
  }

  /**
   * Set since when the name of the field or method is valid.
   *
   * <p>For instance:
   *
   * <pre>
   *   LookUp.fieldOn(clazz).since(8, "foo").since(16, "bar").find();
   *   // will return the field "foo" if the server is running 1.8 or higher, and "bar" if the server
   *   // is running 1.16 or higher.
   * </pre>
   *
   * @param major the major version
   * @param minor the minor version
   * @param name the name in such version
   * @return this instance
   */
  @NonNull
  public NameLookUp<T> since(int major, int minor, @NonNull String name) {
    this.nameMap.put(new Versions.BukkitVersion(major, minor), name);
    return this;
  }

  /**
   * Get the name of the field or method in the current version of Bukkit.
   *
   * @return the name
   */
  @NonNull
  String getName() {
    return this.getName(Versions.getBukkit());
  }

  /**
   * Set since when the name of the field or method is valid.
   *
   * @see #since(int, int, String)
   * @param major the major version
   * @param name the name in such version
   * @return this instance
   */
  @NonNull
  public NameLookUp<T> since(int major, @NonNull String name) {
    return this.since(major, 0, name);
  }
}
