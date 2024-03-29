package com.github.chevyself.reflect;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link Wrapper}. Create it using {@link Wrapper#wrap(Object)}
 *
 * @param <T> the type of the wrapped object
 */
public class AbstractWrapper<T> implements Wrapper<T> {

  @Getter @Setter protected T wrapped;

  /**
   * Wrap an object.
   *
   * @param wrapped the object to be wrapped
   */
  public AbstractWrapper(@Nullable T wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public String toString() {
    return "AbstractWrapper{" + "wrapped=" + wrapped + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    AbstractWrapper<?> that = (AbstractWrapper<?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }
}
