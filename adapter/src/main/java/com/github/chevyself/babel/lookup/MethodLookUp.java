package com.github.chevyself.babel.lookup;

import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that allows to search methods in different versions of Bukkit.
 *
 * @param <O> the type of the object to be returned to the method
 */
class MethodLookUp<O> extends LookUp<O, WrappedMethod<O>> {

  public MethodLookUp(WrappedClass<?> clazz, @Nullable Class<O> type) {
    super(clazz, type);
  }

  @Override
  public @NonNull WrappedMethod<O> find() {
    if (this.declared) {
      return this.clazz.getDeclaredMethod(this.type, this.getName(), this.exact, this.params);
    } else {
      return this.clazz.getMethod(this.type, this.getName(), this.exact, this.params);
    }
  }
}
