package com.github.chevyself.babel.lookup;

import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class that allows to search fields in different versions of Bukkit.
 *
 * @param <O> the type of the object to be returned to the field
 */
class FieldLookUp<O> extends LookUp<O, WrappedField<O>> {

  public FieldLookUp(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    super(clazz, null);
  }

  @Override
  public @NonNull WrappedField<O> find() {
    if (this.declared) {
      return this.clazz.getDeclaredField(this.type, this.getName(), this.exact);
    } else {
      return this.clazz.getField(this.type, this.getName(), this.exact);
    }
  }
}
