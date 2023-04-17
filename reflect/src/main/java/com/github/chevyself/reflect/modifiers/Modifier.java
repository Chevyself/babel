package com.github.chevyself.reflect.modifiers;

import com.github.chevyself.reflect.wrappers.WrappedField;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/**
 * Implement this class to change the value of a field. using {@link WrappedField#set(Object,
 * Modifier)}
 */
public interface Modifier {

  /**
   * Modify a field.
   *
   * @param field the field to be modified
   * @param reference the reference in which the field will be changed
   * @return true if the field was changed successfully
   * @throws InvocationTargetException if the modification fails
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  boolean modify(@NonNull WrappedField<?> field, Object reference)
      throws IllegalAccessException, InvocationTargetException;
}
