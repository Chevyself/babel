package com.github.chevyself.babel.packet.properties;

import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

@SuppressWarnings("unchecked")
public class WrappedPropertyMap extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.properties.PropertyMap");

  @NonNull
  private static final WrappedField<Multimap> PROPERTIES =
      CLAZZ.getDeclaredField(Multimap.class, "properties");

  @NonNull
  private static final WrappedMethod<?> PUT =
      CLAZZ.getMethod("put", String.class, WrappedProperty.CLAZZ.getClazz());

  public WrappedPropertyMap(Object wrapped) {
    super(wrapped);
  }

  public void put(@NonNull String key, @NonNull WrappedProperty value)
      throws IllegalAccessException {
    PROPERTIES.get(this.wrapped).put(key, value.getWrapped());
  }

  @NonNull
  public List<WrappedProperty> get(@NonNull String key) throws IllegalAccessException {
    List<WrappedProperty> properties = new ArrayList<>();
    for (Object object : PROPERTIES.get(this.wrapped).get(key)) {
      if (WrappedProperty.CLAZZ.getClazz().isAssignableFrom(object.getClass())) {
        properties.add(new WrappedProperty(object));
      }
    }
    return properties;
  }
}
