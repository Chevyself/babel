package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.AbstractWrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/** Wrapper for the NMS Entity class. */
public class WrappedEntity extends AbstractWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> CLAZZ = Versions.wrapNmsClassByName("world.entity", "Entity");

  @NonNull
  private static final WrappedMethod<?> SET_LOCATION =
      LookUp.methodOn(WrappedEntity.CLAZZ)
          .findDeclared(true)
          .usingParams(double.class, double.class, double.class, float.class, float.class)
          .since(8, "setLocation")
          .since(18, "a")
          .find();

  @NonNull
  private static final WrappedMethod<?> SET_INVISIBLE =
      LookUp.methodOn(WrappedEntity.CLAZZ)
          .findDeclared(true)
          .usingParams(boolean.class)
          .since(8, "setInvisible")
          .since(18, "j")
          .find();

  @NonNull
  private static final WrappedMethod<Integer> GET_ID =
      LookUp.methodOn(WrappedEntity.CLAZZ, int.class)
          .findDeclared(true)
          .since(8, "getId")
          .since(18, "ae")
          .since(19, 3, "ah")
          .since(19, 4, "af")
          .find();

  @NonNull
  private static final WrappedMethod<?> GET_DATA_WATCHER =
      LookUp.methodOn(WrappedEntity.CLAZZ)
          .findDeclared(true)
          .since(8, "getDataWatcher")
          .since(18, "ai")
          .since(19, 3, "al")
          .since(19, 4, "aj")
          .find();

  /**
   * Method to set the custom name of an entity.
   *
   * <p>Removed in 1.13 and replaced with a method with the same name but with a Component as
   * argument
   */
  @NonNull
  private static final WrappedMethod<?> SET_CUSTOM_NAME =
      WrappedEntity.CLAZZ.getDeclaredMethod("setCustomName", String.class);

  /**
   * Wrap an entity.
   *
   * @param reference the object that must be a NMS Entity.
   */
  protected WrappedEntity(Object reference) {
    super(reference);
  }

  /**
   * Set a custom name for the entity.
   *
   * @param name the name to set
   * @throws PacketHandlingException if the method could not be invoked
   */
  public void setCustomName(String name) throws PacketHandlingException {
    try {
      WrappedEntity.SET_CUSTOM_NAME.invoke(this.wrapped, name);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setCustomName", e);
    }
  }

  /**
   * Sets the location of the entity.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @param yaw the yaw of the view
   * @param pitch the pitch of the view
   * @throws PacketHandlingException if the method could not be invoked
   */
  public void setLocation(double x, double y, double z, float yaw, float pitch)
      throws PacketHandlingException {
    try {
      WrappedEntity.SET_LOCATION.invoke(this.wrapped, x, y, z, yaw, pitch);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setLocation", e);
    }
  }

  /**
   * Sets the visibility of the entity.
   *
   * @param visible true if the entity should be visible, false otherwise
   * @throws PacketHandlingException if the method could not be invoked
   */
  public void setInvisible(boolean visible) throws PacketHandlingException {
    try {
      WrappedEntity.SET_INVISIBLE.invoke(this.wrapped, visible);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setInvisible", e);
    }
  }

  /**
   * Get the id of the entity.
   *
   * @return the id of the entity
   * @throws PacketHandlingException if the method could not be invoked
   */
  public int getId() throws PacketHandlingException {
    try {
      return Objects.requireNonNull(
          WrappedEntity.GET_ID.prepare(this.wrapped), "Entity#getId returned null");
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getId");
    }
  }

  /**
   * Get the data watcher of the entity.
   *
   * @return the data watcher of the entity
   * @throws PacketHandlingException if the method could not be invoked
   */
  @Nullable
  public Object getDataWatcher() throws PacketHandlingException {
    try {
      return WrappedEntity.GET_DATA_WATCHER.invoke(this.wrapped);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getDataWatcher");
    }
  }
}
