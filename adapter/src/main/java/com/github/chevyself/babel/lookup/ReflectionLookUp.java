package com.github.chevyself.babel.lookup;

import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that allows to search for methods or fields in different versions of Bukkit.
 *
 * @param <O> the type of the object to be returned to the field or method
 * @param <T> either {@link WrappedField} or {@link WrappedMethod}
 */
public abstract class ReflectionLookUp<O, T> extends NameLookUp<T> {

  @NonNull protected final WrappedClass<?> clazz;
  @Nullable protected final Class<O> type;
  protected boolean exact = false;
  protected boolean declared = false;
  protected Class<?>[] params = new Class<?>[0];

  ReflectionLookUp(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    this.clazz = clazz;
    this.type = type;
  }

  @Override
  public @NonNull ReflectionLookUp<O, T> since(int major, int minor, @NonNull String name) {
    return (ReflectionLookUp<O, T>) super.since(major, minor, name);
  }

  @Override
  public @NonNull ReflectionLookUp<O, T> since(int major, @NonNull String name) {
    return (ReflectionLookUp<O, T>) super.since(major, name);
  }

  /**
   * Set whether the name, parameters and return type should be exactly the same.
   *
   * @param exact the new exact value
   * @return this instance
   */
  @NonNull
  public ReflectionLookUp<O, T> findExact(boolean exact) {
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
  public ReflectionLookUp<O, T> findDeclared(boolean declared) {
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
  public ReflectionLookUp<O, T> usingParams(Class<?>... params) {
    this.params = params;
    return this;
  }
}
