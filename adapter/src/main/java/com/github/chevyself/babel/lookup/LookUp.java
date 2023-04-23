package com.github.chevyself.babel.lookup;

import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface LookUp<T> {

  /**
   * Look up for a field in the given class and type.
   *
   * @param clazz the class to look up
   * @param type the type of the field
   * @return a new instance
   * @param <O> the type of the field
   */
  @NonNull
  static <O> ReflectionLookUp<O, WrappedField<O>> fieldOn(
      @NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    return new FieldLookUp<>(clazz, null);
  }

  /**
   * Look up for a field in the given class.
   *
   * @param clazz the class to look up
   * @return a new instance
   * @param <O> the type of the field
   */
  @NonNull
  static <O> ReflectionLookUp<O, WrappedField<O>> fieldOn(@NonNull WrappedClass<?> clazz) {
    return new FieldLookUp<>(clazz, null);
  }

  /**
   * Look up for a method in the given class and type.
   *
   * @param clazz the class to look up
   * @param returnType the type of the method
   * @return a new instance
   * @param <O> the type of the method
   */
  @NonNull
  static <O> ReflectionLookUp<O, WrappedMethod<O>> methodOn(
      @NonNull WrappedClass<?> clazz, @Nullable Class<O> returnType) {
    return new MethodLookUp<>(clazz, returnType);
  }

  /**
   * Look up for a method in the given class.
   *
   * @param clazz the class to look up
   * @return a new instance
   * @param <O> the type of the method
   */
  @NonNull
  static <O> ReflectionLookUp<O, WrappedMethod<O>> methodOn(@NonNull WrappedClass<?> clazz) {
    return new MethodLookUp<>(clazz, null);
  }

  @NonNull
  static <O> ClassLookUp<O> forClass() {
    return new ClassLookUp<>();
  }

  /**
   * Find the field or method.
   *
   * @return the field or method
   */
  @NonNull
  T find();
}
