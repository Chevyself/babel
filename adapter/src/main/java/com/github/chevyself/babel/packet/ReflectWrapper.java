package com.github.chevyself.babel.packet;

import com.github.chevyself.reflect.AbstractWrapper;

/** This class is used to wrap an NMS class using reflection. */
public abstract class ReflectWrapper extends AbstractWrapper<Object> {

  /**
   * Create the wrapper.
   *
   * @param wrapped the object to wrap
   * @param check if the object should be checked to be instance of {@link #getReflectClass()}
   */
  public ReflectWrapper(Object wrapped, boolean check) {
    super(wrapped);
    if (check && !this.instanceOf(wrapped)) {
      throw new ClassCastException(wrapped + " is not instance of " + this.getReflectClass());
    }
  }

  /**
   * Create the wrapper. This will check if the object is instance of {@link #getReflectClass()}
   *
   * @param wrapped the object to wrap
   */
  public ReflectWrapper(Object wrapped) {
    this(wrapped, true);
  }

  /** Create the wrapper. */
  public ReflectWrapper() {
    this(null);
  }

  /**
   * Get the class that is being wrapped.
   *
   * @return the class
   */
  public abstract Class<?> getReflectClass();

  @Override
  public void setWrapped(Object wrapped) {
    if (this.instanceOf(wrapped)) {
      this.wrapped = wrapped;
    } else {
      throw new ClassCastException(wrapped + " is not instance of " + this.getReflectClass());
    }
  }

  /**
   * Check if the object is instance of {@link #getReflectClass()}.
   *
   * @param object the object to check
   * @return true if the object is instance of {@link #getReflectClass()}
   */
  private boolean instanceOf(Object object) {
    return object == null
        || (this.getReflectClass() == null
            || this.getReflectClass().isAssignableFrom(object.getClass()));
  }
}
