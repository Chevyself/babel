package me.googas.chat.packet;

import me.googas.reflect.AbstractWrapper;

public abstract class ReflectWrapper extends AbstractWrapper<Object> {

  public ReflectWrapper(Object wrapped, boolean check) {
    super(wrapped);
    if (check && !instanceOf(wrapped)) {
      throw new ClassCastException(wrapped + " is not instance of " + getReflectClass());
    }
  }

  public ReflectWrapper(Object wrapped) {
    this(wrapped, true);
  }

  public ReflectWrapper() {
    this(null);
  }

  public abstract Class<?> getReflectClass();

  @Override
  public void setWrapped(Object wrapped) {
    if (instanceOf(wrapped)) {
      this.wrapped = wrapped;
    } else {
      throw new ClassCastException(wrapped + " is not instance of " + getReflectClass());
    }
  }

  private boolean instanceOf(Object object) {
    return object == null
        || (getReflectClass() == null || getReflectClass().isAssignableFrom(object.getClass()));
  }
}
