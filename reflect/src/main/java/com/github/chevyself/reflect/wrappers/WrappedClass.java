package com.github.chevyself.reflect.wrappers;

import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.debug.LogTask;
import com.github.chevyself.reflect.util.ReflectUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class wraps a {@link Class} to use its methods checking if those can be executed and
 * returning objects without throwing errors.
 *
 * <p>The method {@link Class#forName(String)} would throw a {@link ClassNotFoundException}
 * meanwhile {@link WrappedClass#forName(String)} will just return an empty instance if that is the
 * case most of the methods declared in this class would return empty instances too
 *
 * @param <O> the type of the class object
 */
public final class WrappedClass<O> extends LangWrapper<Class<O>> {

  private WrappedClass(@Nullable Class<O> clazz) {
    super(clazz);
  }

  private WrappedClass() {
    this(null);
  }

  /**
   * Return the wrapper of the {@link Class} object if {@link Class#forName(String)} matches a class
   * else it would be empty.
   *
   * @see Class#forName(String)
   * @param name the fully qualified name of the class
   * @return the wrapped {@link Class} instance
   */
  @NonNull
  public static WrappedClass<?> forName(@NonNull String name) {
    try {
      return new WrappedClass<>(Class.forName(name));
    } catch (ClassNotFoundException e) {
      Debugger.getInstance().getLogger().severe("Could not find class " + name);
      return new WrappedClass<>();
    }
  }

  /**
   * Wrap a {@link Class} instance.
   *
   * @param clazz the class to wrap
   * @return the wrapper of {@link Class}
   * @param <T> the type of the class object
   */
  @NonNull
  public static <T> WrappedClass<T> of(@NonNull Class<T> clazz) {
    return new WrappedClass<>(clazz);
  }

  @NonNull
  private static List<WrappedField<?>> wrap(@NonNull Field... fields) {
    List<WrappedField<?>> wrappers = new ArrayList<>(fields.length);
    for (Field field : fields) {
      wrappers.add(WrappedField.of(field));
    }
    return wrappers;
  }

  @NonNull
  private static List<WrappedMethod<?>> wrap(@NonNull Method... methods) {
    List<WrappedMethod<?>> wrappers = new ArrayList<>(methods.length);
    for (Method method : methods) {
      wrappers.add(WrappedMethod.of(method));
    }
    return wrappers;
  }

  private static boolean compareMethod(
      @Nullable Class<?> returnType,
      @NonNull String name,
      @NonNull Method method,
      @NonNull Class<?>[] params,
      boolean exact) {
    if (method.getName().equals(name)) {
      Class<?>[] paramTypes = method.getParameterTypes();
      return ReflectUtil.compareParameters(paramTypes, params, exact)
          && (returnType == null
              || (exact
                  ? returnType.equals(method.getReturnType())
                  : returnType.isAssignableFrom(method.getReturnType())));
    }
    return false;
  }

  /**
   * Get the constructor matching the given parameters.
   *
   * @param params the parameters to match the constructor with
   * @return a {@link WrappedConstructor} instance containing the constructor or empty if not found
   */
  @NonNull
  public WrappedConstructor<O> getConstructor(@NonNull Class<?>... params) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe(
              "Trying to get constructor in a null class with the params "
                  + Arrays.toString(params));
      return WrappedConstructor.of(null);
    }
    LogTask task =
        Debugger.getInstance()
            .logTask(
                "Getting constructor for "
                    + this.wrapped.getName()
                    + " with params "
                    + Arrays.toString(params));
    Constructor<O> constructor = null;
    for (Constructor<?> referenceConstructor : this.wrapped.getConstructors()) {
      if (this.compare(referenceConstructor, params, false)) {
        //noinspection unchecked
        constructor = (Constructor<O>) referenceConstructor;
        break;
      }
    }
    if (constructor == null) {
      task.end("Constructor not found");
    }
    return WrappedConstructor.of(constructor);
  }

  /**
   * Get the field matching the name.
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField<?> getField(@NonNull String name) {
    return this.getField(null, name, false);
  }

  /**
   * Get the field matching the name.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name to match the field with
   * @param exact whether the field type should be exactly the same as the given type
   * @return a {@link WrappedField} instance containing the field or empty if not found
   * @param <T> the type of the object that the field contains
   */
  @NonNull
  public <T> WrappedField<T> getField(
      @Nullable Class<T> fieldType, @NonNull String name, boolean exact) {
    return this.getField(
        clazz -> {
          try {
            return clazz.getField(name);
          } catch (NoSuchFieldException e) {
            Debugger.getInstance()
                .getLogger()
                .severe("Could not find field " + name + " in class " + clazz.getName());
            return null;
          }
        },
        name,
        fieldType,
        exact);
  }

  @NonNull
  private <T> WrappedField<T> getField(
      @NonNull Function<Class<?>, Field> supplier,
      @NonNull String name,
      @Nullable Class<T> fieldType,
      boolean exact) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe("Trying to get field in a null class with the name " + name);
      return WrappedField.of(fieldType, null);
    }
    Field field = supplier.apply(this.wrapped);
    if (fieldType != null
        && (exact
            ? !fieldType.equals(field.getType())
            : !fieldType.isAssignableFrom(field.getType()))) {
      field = null;
    }
    return WrappedField.of(fieldType, field);
  }

  /**
   * Get a declared field matching the name.
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField<?> getDeclaredField(@NonNull String name) {
    return this.getDeclaredField(null, name, false);
  }

  /**
   * Get a declared field matching the name.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   * @param <T> the type of the object that the field contains
   */
  @NonNull
  public <T> WrappedField<T> getDeclaredField(@Nullable Class<T> fieldType, @NonNull String name) {
    return this.getDeclaredField(fieldType, name, false);
  }

  /**
   * Get a declared field matching the name.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name to match the field with
   * @param exact whether the field type should be exactly the same as the given type
   * @return a {@link WrappedField} instance containing the field or empty if not found
   * @param <T> the type of the object that the field contains
   */
  @NonNull
  public <T> WrappedField<T> getDeclaredField(
      @Nullable Class<T> fieldType, @NonNull String name, boolean exact) {
    return this.getField(
        (clazz) -> {
          try {
            return clazz.getField(name);
          } catch (NoSuchFieldException e) {
            Debugger.getInstance()
                .getLogger()
                .severe("Could not find field " + name + " in class " + clazz.getName());
            return null;
          }
        },
        name,
        fieldType,
        exact);
  }

  @NonNull
  private <T> WrappedMethod<T> getMethod(
      @NonNull Function<@NonNull Class<?>, @NonNull Method[]> function,
      @Nullable Class<T> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe(
              "Trying to get method in a null class with the name "
                  + name
                  + " and params "
                  + Arrays.toString(params));
      return WrappedMethod.of(null, returnType);
    }
    for (Method referenceMethod : function.apply(this.wrapped)) {
      if (WrappedClass.compareMethod(returnType, name, referenceMethod, params, exact)) {
        return WrappedMethod.of(referenceMethod, returnType);
      }
    }
    Debugger.getInstance()
        .getLogger()
        .severe(
            "Could not find method "
                + name
                + " in class "
                + this.wrapped.getName()
                + " with params "
                + Arrays.toString(params));
    return WrappedMethod.of(null, returnType);
  }

  /**
   * Get a method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getMethod(
      @Nullable Class<T> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    return this.getMethod(Class::getMethods, returnType, name, exact, params);
  }

  /**
   * Get a method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getMethod(
      @Nullable Class<T> returnType, @NonNull String name, @NonNull Class<?>... params) {
    return this.getMethod(returnType, name, false, params);
  }

  /**
   * Get a method matching the name, parameter types and return type.
   *
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getMethod(@NonNull String name, @NonNull Class<?>... params) {
    return this.getMethod(null, name, params);
  }

  /**
   * Get a declared method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getDeclaredMethod(
      @Nullable Class<T> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    return this.getMethod(Class::getDeclaredMethods, returnType, name, exact, params);
  }

  /**
   * Get a declared method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getDeclaredMethod(
      @Nullable Class<T> returnType, @NonNull String name, @NonNull Class<?>... params) {
    return this.getDeclaredMethod(returnType, name, false, params);
  }

  /**
   * Get a declared method matching the name, parameter types and return type.
   *
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getDeclaredMethod(@NonNull String name, @NonNull Class<?>... params) {
    return this.getDeclaredMethod(null, name, params);
  }

  private boolean hasMethod(
      @NonNull Function<@NonNull Class<?>, @NonNull Method[]> function,
      @Nullable Class<?> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe(
              "Trying to check for a method in a null class with the name "
                  + name
                  + " and params "
                  + Arrays.toString(params));
      return false;
    }
    for (Method referenceMethod : function.apply(this.wrapped)) {
      if (WrappedClass.compareMethod(returnType, name, referenceMethod, params, exact)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasMethod(
      @Nullable Class<?> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    return this.hasMethod(Class::getMethods, returnType, name, exact, params);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasMethod(
      @Nullable Class<?> returnType, @NonNull String name, @NonNull Class<?>... params) {
    return this.hasMethod(returnType, name, false, params);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param name the name of the method to find
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasMethod(@NonNull String name, @NonNull Class<?>... params) {
    return this.hasMethod(null, name, params);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasDeclaredMethod(
      @Nullable Class<?> returnType,
      @NonNull String name,
      boolean exact,
      @NonNull Class<?>... params) {
    return this.hasMethod(Class::getMethods, returnType, name, exact, params);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasDeclaredMethod(
      @Nullable Class<?> returnType, @NonNull String name, @NonNull Class<?>... params) {
    return this.hasMethod(returnType, name, false, params);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param name the name of the method to find
   * @param exact whether the parameter types and return types should be exactly the same as the
   *     given types
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasDeclaredMethod(@NonNull String name, @NonNull Class<?>... params) {
    return this.hasMethod(null, name, params);
  }

  private boolean hasField(
      @NonNull Function<@NonNull Class<?>, @NonNull Field[]> function,
      @Nullable Class<?> fieldType,
      @NonNull String name,
      boolean exact) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe("Trying to check for a field in a null class with the name " + name);
      return false;
    }
    for (Field field : this.wrapped.getFields()) {
      if (field.getName().equals(name)
          && (fieldType == null
              || (exact
                  ? fieldType.equals(field.getType())
                  : fieldType.isAssignableFrom(field.getType())))) return true;
    }
    return false;
  }

  /**
   * Checks if a field with the given name exists in the class.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name of the field to find
   * @param exact whether the field type should be exactly the same as the given type
   * @return true if the field is found false otherwise
   */
  public boolean hasField(@Nullable Class<?> fieldType, @NonNull String name, boolean exact) {
    return this.hasField(Class::getFields, fieldType, name, exact);
  }

  /**
   * Checks if a declared field with the given name exists in the class.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name of the field to find
   * @param exact whether the field type should be exactly the same as the given type
   * @return true if the field is found false otherwise
   */
  public boolean hasDeclaredField(
      @Nullable Class<?> fieldType, @NonNull String name, boolean exact) {
    return this.hasField(Class::getDeclaredFields, fieldType, name, exact);
  }

  /**
   * Checks if a constructor with the given parameter types exists in the class.
   *
   * @param params the parameters of the constructor to find
   * @return true if the constructor is found false otherwise
   */
  public boolean hasConstructor(Class<?>... params) {
    if (this.wrapped == null) {
      Debugger.getInstance()
          .getLogger()
          .severe(
              "Trying to check for a constructor in a null class with the parameters "
                  + Arrays.toString(params));
      return false;
    }
    for (Constructor<?> constructor : this.wrapped.getConstructors()) {
      if (this.compare(constructor, params, false)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Compares a constructor to an array of classes. This checks if the constructor has the same
   * parameter types as the array
   *
   * @param constructor the constructor to be compared
   * @param params the array to compare
   * @param exact whether the parameter types should be exactly the same as the given types
   * @return true if the parameter types of the constructor matches the classes of the array
   */
  private boolean compare(
      @NonNull Constructor<?> constructor, @NonNull Class<?>[] params, boolean exact) {
    return ReflectUtil.compareParameters(constructor.getParameterTypes(), params, exact);
  }

  /**
   * Get a list of {@link Method} of the class.
   *
   * @return the list of methods
   */
  @NonNull
  public List<WrappedMethod<?>> getMethods() {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Trying to get methods from a null class");
    }
    return wrapped == null ? new ArrayList<>() : WrappedClass.wrap(this.wrapped.getMethods());
  }

  /**
   * Get a list of {@link Field} of the class.
   *
   * @return the list of fields
   */
  @NonNull
  public List<WrappedField<?>> getFields() {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Trying to get methods from a null class");
    }
    return this.wrapped == null ? new ArrayList<>() : WrappedClass.wrap(this.wrapped.getFields());
  }

  /**
   * Get a list of {@link Field} that are declared in the class.
   *
   * @return the list of fields
   */
  @NonNull
  public List<WrappedField<?>> getDeclaredFields() {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Trying to get methods from a null class");
    }
    return this.wrapped == null
        ? new ArrayList<>()
        : WrappedClass.wrap(this.wrapped.getDeclaredFields());
  }

  /**
   * Get the wrapped class.
   *
   * @return the wrapped class
   */
  public Class<O> getClazz() {
    return this.wrapped;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedClass<?> that = (WrappedClass<?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedClass.class.getSimpleName() + "[", "]")
        .add("clazz=" + wrapped)
        .toString();
  }

  /**
   * Create a new instance of the class.
   *
   * <p>This will first attempt to use the empty constructor. If that is not found it will attempt
   * to use the first constructor it finds and populate it with default values.
   *
   * @return the list of constructors
   * @throws IllegalAccessException if the constructor is not accessible
   * @throws InstantiationException if the class cannot be instantiated
   * @throws InvocationTargetException if the constructor throws an exception
   */
  @Nullable
  public O newInstance()
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    if (this.wrapped == null) {
      Debugger.getInstance().getLogger().severe("Trying to create a new instance of a null class");
      return null;
    }
    WrappedConstructor<O> emptyConstructor = this.getConstructor();
    if (emptyConstructor.isPresent()) {
      return emptyConstructor.invoke();
    } else {
      for (Constructor<?> constructor : this.wrapped.getConstructors()) {
        Object[] params = new Object[constructor.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
          params[i] = ReflectUtil.getDefaultValue(constructor.getParameterTypes()[i]);
        }
        //noinspection unchecked
        return (O) constructor.newInstance(params);
      }
    }
    return null;
  }

  /**
   * Gets the wrapped class as an array class.
   *
   * @see ReflectUtil#getArrayClass(WrappedClass)
   * @return the array class
   * @throws IllegalArgumentException if the class is null
   */
  @NonNull
  public Class<?> getArrayClazz() {
    return ReflectUtil.getArrayClass(this);
  }
}
