package com.github.chevyself.reflect;

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
  static <T> Wrapper<T> wrap(T object) {
    return new AbstractWrapper<>(object);
  }

  /**
   * Get the wrapped object.
   *
   * @return the wrapped object
   */
  T getWrapped();

  /**
   * Check if {@link #getWrapped()} is not null.
   *
   * @return true if handle is not null
   */
  default boolean isPresent() {
    return this.getWrapped() != null;
  }

  /**
   * Set the wrapped object.
   *
   * @deprecated Use {@link SetterWrapper}
   * @param object the new wrapped object
   * @return this same instance
   */
  default Wrapper<T> set(T object) {
    throw new UnsupportedOperationException("Deprecated");
  }
}
