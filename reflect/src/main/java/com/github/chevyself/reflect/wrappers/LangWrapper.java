package com.github.chevyself.reflect.wrappers;

import com.github.chevyself.reflect.Wrapper;
import java.util.Objects;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of wrapper for classes located in the Java package 'java.lang.reflect'
 *
 * @param <T> the wrapped type
 */
class LangWrapper<T> implements Wrapper<T> {

  @Getter @Nullable final T wrapped;

  LangWrapper(@Nullable T wrapped) {
    this.wrapped = wrapped;
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
