package me.googas.chat.packet;

import me.googas.reflect.AbstractWrapper;

/**
 * This class is used to wrap an NMS class using reflection.
 */
public abstract class ReflectWrapper extends AbstractWrapper<Object> {

  /**
   * Create the wrapper.
   *
   * @param wrapped the object to wrap
   * @param check if the object should be checked to be instance of {@link #getReflectClass()}
   */
  public ReflectWrapper(Object wrapped, boolean check) {
    super(wrapped);
    if (check && !instanceOf(wrapped)) {
      throw new ClassCastException(wrapped + " is not instance of " + getReflectClass());
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
   * Get the class that is being wrapped
   *
   * @return the class
   */
  public abstract Class<?> getReflectClass();

  /**
   *
   * @param wrapped the new wrapped object
   */
  @Override
  public void setWrapped(Object wrapped) {
    if (instanceOf(wrapped)) {
      this.wrapped = wrapped;
    } else {
      throw new ClassCastException(wrapped + " is not instance of " + getReflectClass());
    }
  }

    /**
     * Check if the object is instance of {@link #getReflectClass()}
     *
     * @param object the object to check
     * @return true if the object is instance of {@link #getReflectClass()}
     */
  private boolean instanceOf(Object object) {
    return object == null
        || (getReflectClass() == null || getReflectClass().isAssignableFrom(object.getClass()));
  }
}
