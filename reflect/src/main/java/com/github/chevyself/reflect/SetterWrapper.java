package com.github.chevyself.reflect;

import org.jetbrains.annotations.Nullable;

/**
 * Wraps an element and allows it to be set.
 *
 * @param <T> the type of the object to be wrapped
 */
@Deprecated
public interface SetterWrapper<T> extends Wrapper<T> {

  /**
   * Set the wrapped object.
   *
   * @param object the new wrapped object
   */
  @Deprecated
  void setWrapped(@Nullable T object);
}
