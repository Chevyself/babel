package com.github.chevyself.reflect.wrappers;

import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.modifiers.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** This class wraps a {@link Field} to set or get the declaration. */
public final class WrappedField<O> extends LangWrapper<Field> {

  @Nullable private final Class<O> fieldType;

  private WrappedField(@Nullable Field reference, @Nullable Class<O> fieldType) {
    super(reference);
    this.fieldType = fieldType;
  }

  WrappedField() {
    this(null, null);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param field the field to wrap
   * @return the wrapper of the field
   */
  @NonNull
  public static WrappedField<?> of(@Nullable Field field) {
    return WrappedField.of(null, field);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param fieldType the class of the object that the field contains
   * @param field the field to wrap
   * @param <T> the type of the object in the field
   * @return the wrapped field
   */
  @NonNull
  public static <T> WrappedField<T> of(@Nullable Class<T> fieldType, @Nullable Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField<>(field, fieldType);
  }

  /**
   * Get the value that is stored in the field for the parameter object.
   *
   * @param instance the object to get the value of the field from
   * @return the object from the field
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is inaccessible.
   */
  @Nullable
  public Object provide(@Nullable Object instance) throws IllegalAccessException {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Attempting to get the object in a null field");
      return null;
    }
    return this.wrapped.get(instance);
  }

  /**
   * Get the value that is stored in the field for the parameter object and cast it to the field
   * type.
   *
   * @param instance the object to get the value of the field from
   * @return the object from the field
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  @Nullable
  public O get(@Nullable Object instance) throws IllegalAccessException {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Attempting to get the object in a null field");
      return null;
    }
    if (this.fieldType == null) {
      Debugger.getInstance()
          .getLogger()
          .severe("Field has no type, cannot cast the object to the field type @ " + this.wrapped);
      return null;
    }
    return this.fieldType.cast(this.provide(instance));
  }

  /**
   * Set the value of the field in an object.
   *
   * @param object the object to set the value of the field to
   * @param value the new value to set on the field
   * @return whether the value has been set successfully
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  public boolean set(@Nullable Object object, @Nullable Object value) throws IllegalAccessException {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Attempting to set the object: '" + object + "' in a null field");
      return false;
    }
    this.wrapped.set(object, value);
    return true;
  }

  /**
   * Set the value of the field using a modifier.
   *
   * @param object the object to set the value of the field to
   * @param modifier the modifier which will change the value of the field
   * @return true if the value has been changed
   * @throws InvocationTargetException if the modification fails
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  public boolean set(@Nullable Object object, @NonNull Modifier modifier)
      throws InvocationTargetException, IllegalAccessException {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Attempting to set the object in a null field, using the modifier: " + modifier);
      return false;
    }
    return modifier.modify(this, object);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedField.class.getSimpleName() + "[", "]")
        .add("field=" + wrapped)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedField<?> that = (WrappedField<?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }
}
