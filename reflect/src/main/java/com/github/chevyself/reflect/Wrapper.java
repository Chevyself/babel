package com.github.chevyself.reflect;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wraps an element.
 *
 * @param <T> the type of element to be wrapped
 */
public interface Wrapper<T> {

  /**
   * Wrap an element.
   *
   * @param object the object to be wrapped
   * @return the wrapped object
   * @param <T> the type of the wrapped object
   */
  @NonNull
  static <T> Wrapper<@Nullable T> wrap(@Nullable T object) {
    return new AbstractWrapper<>(object);
  }

  /**
   * Get the wrapped object.
   *
   * @return the wrapped object
   */
  @Nullable
  T getWrapped();

  /**
   * Check if {@link #getWrapped()} is not null.
   *
   * @return true if handle is not null
   */
  default boolean isPresent() {
    return this.getWrapped() != null;
  }
}
