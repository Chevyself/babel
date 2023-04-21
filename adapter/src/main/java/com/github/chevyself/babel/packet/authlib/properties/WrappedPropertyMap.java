package com.github.chevyself.babel.packet.authlib.properties;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

/** Wrapper for the PropertyMap class. */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WrappedPropertyMap extends AbstractWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("com.mojang.authlib.properties.PropertyMap");

  @NonNull
  private static final WrappedField<Multimap> PROPERTIES =
      WrappedPropertyMap.CLAZZ.getDeclaredField(Multimap.class, "properties");

  @NonNull
  private static final WrappedMethod<?> PUT =
      WrappedPropertyMap.CLAZZ.getMethod(
          "put",
          String.class,
          com.github.chevyself.babel.packet.authlib.properties.WrappedProperty.CLAZZ.getClazz());

  /**
   * Cerate a new WrappedPropertyMap.
   *
   * @param wrapped the object that must be a PropertyMap
   */
  public WrappedPropertyMap(Object wrapped) {
    super(wrapped);
  }

  /**
   * Puts a property into the map.
   *
   * @param key the key of the property
   * @param value the value of the property
   * @throws PacketHandlingException if the property could not be put into the map
   */
  public void put(
      @NonNull String key,
      @NonNull com.github.chevyself.babel.packet.authlib.properties.WrappedProperty value)
      throws PacketHandlingException {
    try {
      Multimap multimap = WrappedPropertyMap.PROPERTIES.get(this.wrapped);
      if (multimap != null) {
        multimap.put(key, value.getWrapped());
      } else {
        Debugger.getInstance().getLogger().severe("Could not access properties in " + this.wrapped);
      }
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Failed to put property into PropertyMap", e);
    }
  }

  /**
   * Gets a list of properties from the map.
   *
   * @param key the key of the properties
   * @return a list of properties
   * @throws PacketHandlingException if the properties could not be retrieved
   */
  @NonNull
  public List<com.github.chevyself.babel.packet.authlib.properties.WrappedProperty> get(
      @NonNull String key) throws PacketHandlingException {
    List<com.github.chevyself.babel.packet.authlib.properties.WrappedProperty> properties =
        new ArrayList<>();
    try {
      Multimap multimap = WrappedPropertyMap.PROPERTIES.get(this.wrapped);
      if (multimap == null) {
        Debugger.getInstance().getLogger().severe("Could not access properties in " + this.wrapped);
        return properties;
      }
      for (Object object : multimap.get(key)) {
        if (com.github.chevyself.babel.packet.authlib.properties.WrappedProperty.CLAZZ
            .getClazz()
            .isAssignableFrom(object.getClass())) {
          properties.add(
              new com.github.chevyself.babel.packet.authlib.properties.WrappedProperty(object));
        }
      }
      return properties;
    } catch (IllegalAccessException e) {
      throw new PacketHandlingException("Failed to get properties from PropertyMap", e);
    }
  }
}
