package com.github.chevyself.reflect.util;

import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Static utilities for java reflection. */
public final class ReflectUtil {

  public static final Map<Class<?>, Class<?>> boxing = new HashMap<>();

  private static final Map<Class<?>, Object> defValues = new HashMap<>();

  static {
    ReflectUtil.boxing.put(boolean.class, Boolean.class);
    ReflectUtil.boxing.put(byte.class, Byte.class);
    ReflectUtil.boxing.put(short.class, Short.class);
    ReflectUtil.boxing.put(char.class, Character.class);
    ReflectUtil.boxing.put(int.class, Integer.class);
    ReflectUtil.boxing.put(long.class, Long.class);
    ReflectUtil.boxing.put(float.class, Float.class);
    ReflectUtil.boxing.put(double.class, Double.class);

    ReflectUtil.defValues.put(boolean.class, false);
    ReflectUtil.defValues.put(byte.class, (byte) 0);
    ReflectUtil.defValues.put(short.class, (short) 0);
    ReflectUtil.defValues.put(char.class, (char) 0);
    ReflectUtil.defValues.put(int.class, 0);
    ReflectUtil.defValues.put(long.class, 0L);
    ReflectUtil.defValues.put(float.class, 0F);
    ReflectUtil.defValues.put(double.class, 0D);
    ReflectUtil.defValues.put(Collection.class, new ArrayList<>());
  }

  /**
   * Check whether an array of annotations has certain annotation.
   *
   * @param annotations the array of annotations to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the array has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<? extends Annotation> clazz) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType() == clazz) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the annotation inside an array.
   *
   * @param annotations the array of annotations to get the annotation from
   * @param clazz the class of the annotation to get
   * @param <T> the type of the annotation to get
   * @return the annotation if found
   * @throws IllegalArgumentException if the array does not contain the annotation use {@link
   *     #hasAnnotation(Method, Class)} to avoid this
   */
  @NonNull
  public static <T extends Annotation> T getAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<T> clazz) {
    for (Annotation annotation : annotations) {
      if (clazz.isAssignableFrom(annotation.annotationType())) {
        return clazz.cast(annotation);
      }
    }
    throw new IllegalArgumentException(
        Arrays.toString(annotations) + " does not contain the annotation " + clazz);
  }

  /**
   * Check whether a method has certain annotation.
   *
   * @param method the method to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the method has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Method method, @NonNull Class<? extends Annotation> clazz) {
    return ReflectUtil.hasAnnotation(method.getAnnotations(), clazz);
  }

  /**
   * Compare two arrays of {@link Class}. This will check that the classes are in the same indexes
   * for both arrays
   *
   * @param paramTypes the first array
   * @param params the second array
   * @param exact if true it will check that the classes are the same, if false it will check that
   *     the classes are assignable from each other
   * @return true if both arrays have the same elements
   */
  public static boolean compareParameters(
      @NonNull Class<?>[] paramTypes, @NonNull Class<?>[] params, boolean exact) {
    if (paramTypes == null || params == null) return true;
    if (paramTypes.length != params.length) return false;
    for (int i = 0; i < paramTypes.length; i++) {
      Class<?> a = paramTypes[i];
      Class<?> b = params[i];
      if (a == null || b == null) return true;
      if (exact ? !a.equals(b) : !a.isAssignableFrom(b)) return false;
    }
    return true;
  }

  /**
   * Compare two arrays of {@link Class}. This will check that the classes are in the same indexes
   *
   * @deprecated this does not allow to check if the classes are assignable from each other, use
   *     {@link #compareParameters(Class[], Class[], boolean)} instead
   * @param paramTypes the first array
   * @param params the second array
   * @return true if both arrays have the same elements
   */
  @Deprecated
  public static boolean compareParameters(Class<?>[] paramTypes, Class<?>[] params) {
    return ReflectUtil.compareParameters(paramTypes, params, false);
  }

  /**
   * Get the class that boxes a primitive class.
   *
   * <p>EJ: For the primitive class 'long' its box will be {@link Long}
   *
   * @param primitive the primitive class to box
   * @param <T> the type of the primitive
   * @return the boxing class
   */
  @NonNull
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getBoxing(@NonNull Class<T> primitive) {
    return (Class<T>) ReflectUtil.boxing.get(primitive);
  }

  /**
   * Checks that an object is not null else throws an exception.
   *
   * @param o the object to check
   * @param exception the exception to throw
   * @return the object if it is not null
   * @param <O> the type of the object
   * @param <T> the type of the exception
   * @throws T if the object is null
   */
  @NonNull
  public static <O, T extends Exception> O nonNull(@Nullable O o, @NonNull T exception) throws T {
    if (o == null) throw exception;
    return o;
  }

  /**
   * Checks that an object inside a wrapper is not null else throws an exception.
   *
   * @see #nonNull(Object, Exception)
   * @param wrapper the wrapper to get the object from
   * @param exception the exception to throw
   * @return the object if it is not null
   * @param <O> the type of the object
   * @param <T> the type of the exception
   * @throws T if the object is null
   */
  @NonNull
  public static <O, T extends Exception> O nonNullWrapped(
      @NonNull Wrapper<O> wrapper, @NonNull T exception) throws T {
    return ReflectUtil.nonNull(wrapper.getWrapped(), exception);
  }

  /**
   * Gets the default value of a class.
   *
   * <p>For example, for the class {@link Integer} the default value is 0
   *
   * <p>This values are inside of {@link ReflectUtil#defValues}
   *
   * @param parameterType the class to get the default value from
   * @return the default value
   */
  @Nullable
  public static Object getDefaultValue(@NonNull Class<?> parameterType) {
    return ReflectUtil.defValues.get(parameterType);
  }

  /**
   * Get the array class of a wrapper.
   *
   * @param wrapper the wrapper to get the array class from
   * @return the array class
   */
  @NonNull
  public static Class<?> getArrayClass(@NonNull WrappedClass<?> wrapper) {
    Class<?> clazz = wrapper.getClazz();
    if (clazz == null) {
      throw new IllegalArgumentException("Cannot get array class from null");
    } else {
      try {
        return Class.forName("[L" + clazz.getCanonicalName() + ";");
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException("Cannot get array class from " + clazz);
      }
    }
  }

  /**
   * Safe unboxing of an {@link Integer}.
   *
   * @param integer the integer to unbox
   * @param def the default value if the integer is null
   * @return the unboxed integer or the default value if the integer is null
   */
  public static int safelyUnbox(Integer integer, int def) {
    return integer == null ? def : integer;
  }
}
