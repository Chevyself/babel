package com.github.chevyself.babel.lookup;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows to look up for a class in different Bukkit versions.
 *
 * @param <O> the type of the class
 */
public class ClassLookUp<O> extends NameLookUp<WrappedClass<O>> {

  @Nullable private String addedPackage;

  ClassLookUp() {}

  /**
   * Sets in which package the class is located after the Bukkit version 1.17.
   *
   * @param addedPackage the package name
   * @return this instance
   */
  @NonNull
  public ClassLookUp<O> atPackage(@NonNull String addedPackage) {
    this.addedPackage = addedPackage;
    return this;
  }

  @Override
  public @NonNull String getName(@NonNull Versions.BukkitVersion bukkit) {
    return Versions.getCanonicalName(this.addedPackage, super.getName(bukkit));
  }

  @Override
  public @NonNull ClassLookUp<O> since(int major, int minor, @NonNull String name) {
    return (ClassLookUp<O>) super.since(major, minor, name);
  }

  @Override
  public @NonNull ClassLookUp<O> since(int major, @NonNull String name) {
    return (ClassLookUp<O>) super.since(major, name);
  }

  @Override
  public @NonNull WrappedClass<O> find() {
    //noinspection unchecked
    return (WrappedClass<O>) WrappedClass.forName(this.getName());
  }
}
