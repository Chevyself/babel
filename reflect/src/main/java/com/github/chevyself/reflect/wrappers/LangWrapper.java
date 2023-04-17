package com.github.chevyself.reflect.wrappers;

import com.github.chevyself.reflect.Wrapper;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

/**
 * Implementation of wrapper for classes located in the Java package 'java.lang.reflect'
 *
 * @param <T> the wrapped type
 */
class LangWrapper<T> implements Wrapper<T> {

  @Getter final T wrapped;

  LangWrapper(T wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  @Deprecated
  public @NonNull LangWrapper<T> set(T object) {
    throw new UnsupportedOperationException("References in LangWrappers are final");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    LangWrapper<?> that = (LangWrapper<?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }
}
