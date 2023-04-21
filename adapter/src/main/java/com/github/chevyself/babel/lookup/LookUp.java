package com.github.chevyself.babel.lookup;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.util.ReflectUtil;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.util.TreeMap;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that allows to search for methods or fields in different versions of Bukkit.
 *
 * @param <O> the type of the object to be returned to the field or method
 * @param <T> either {@link WrappedField} or {@link WrappedMethod}
 */
public abstract class LookUp<O, T> {

  @NonNull protected final TreeMap<Versions.BukkitVersion, String> nameMap = new TreeMap<>();
  @NonNull protected final WrappedClass<?> clazz;
  @Nullable protected final Class<O> type;
  protected boolean exact = false;
  protected boolean declared = false;
  protected Class<?>[] params = new Class<?>[0];

  LookUp(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    this.clazz = clazz;
    this.type = type;
  }

  /**
   * Look up for a field in the given class and type.
   *
   * @param clazz the class to look up
   * @param type the type of the field
   * @return a new instance
   * @param <O> the type of the field
   */
  @NonNull
  public static <O> LookUp<O, WrappedField<O>> fieldOn(
      @NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    return new FieldLookUp<>(clazz, null);
  }

  /**
   * Look up for a field in the given class.
   *
   * @param clazz the class to look up
   * @return a new instance
   * @param <O> the type of the field
   */
  @NonNull
  public static <O> LookUp<O, WrappedField<O>> fieldOn(@NonNull WrappedClass<?> clazz) {
    return new FieldLookUp<>(clazz, null);
  }

  /**
   * Look up for a method in the given class and type.
   *
   * @param clazz the class to look up
   * @param returnType the type of the method
   * @return a new instance
   * @param <O> the type of the method
   */
  @NonNull
  public static <O> LookUp<O, WrappedMethod<O>> methodOn(
      @NonNull WrappedClass<?> clazz, @Nullable Class<O> returnType) {
    return new MethodLookUp<>(clazz, returnType);
  }

  /**
   * Look up for a method in the given class.
   *
   * @param clazz the class to look up
   * @return a new instance
   * @param <O> the type of the method
   */
  @NonNull
  public static <O> LookUp<O, WrappedMethod<O>> methodOn(@NonNull WrappedClass<?> clazz) {
    return new MethodLookUp<>(clazz, null);
  }

  /**
   * Set whether the name, parameters and return type should be exactly the same.
   *
   * @param exact the new exact value
   * @return this instance
   */
  @NonNull
  public LookUp<O, T> findExact(boolean exact) {
    this.exact = exact;
    return this;
  }

  /**
   * Set whether the field or method should be declared or not.
   *
   * @param declared the new declared value
   * @return this instance
   */
  @NonNull
  public LookUp<O, T> findDeclared(boolean declared) {
    this.declared = declared;
    return this;
  }

  /**
   * Set the parameters of the method to be looked up.
   *
   * @param params the parameters
   * @return this instance
   */
  @NonNull
  public LookUp<O, T> usingParams(Class<?>... params) {
    this.params = params;
    return this;
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
  public LookUp<O, T> since(int major, int minor, @NonNull String name) {
    this.nameMap.put(new Versions.BukkitVersion(major, minor), name);
    return this;
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
  public LookUp<O, T> since(int major, @NonNull String name) {
    return this.since(major, 0, name);
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
   * Find the field or method.
   *
   * @return the field or method
   */
  @NonNull
  public abstract T find();
}
