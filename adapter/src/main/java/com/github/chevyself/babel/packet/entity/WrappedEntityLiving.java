package com.github.chevyself.babel.packet.entity;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.lookup.LookUp;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** Wrapper for the NMS EntityLiving class. */
public class WrappedEntityLiving extends WrappedEntity {

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      Versions.wrapNmsClassByName("world.entity", "EntityLiving");

  @NonNull
  private static final WrappedMethod<Float> GET_MAX_HEALTH =
      LookUp.methodOn(WrappedEntityLiving.CLAZZ, float.class)
          .findDeclared(true)
          .since(8, "getMaxHealth")
          .since(18, "ef")
          .since(18, 2, "em")
          .since(19, "eu")
          .since(19, 1, "et")
          .since(19, 3, "ez")
          .since(19, 4, "eE")
          .find();
  // WrappedEntityWither.ENTITY_WITHER.getMethod(float.class, "getMaxHealth");

  @NonNull
  private static final WrappedMethod<?> SET_HEALTH =
      LookUp.methodOn(WrappedEntityLiving.CLAZZ)
          .findDeclared(true)
          .usingParams(float.class)
          .since(8, "setHealth")
          .since(18, "c")
          .find();

  // WrappedEntityWither.ENTITY_WITHER.getMethod("setHealth", float.class);

  /**
   * Wrap an entity.
   *
   * @param reference the object that must be a NMS EntityLiving.
   */
  protected WrappedEntityLiving(Object reference) {
    super(reference);
  }

  /**
   * Get the max health of the entity.
   *
   * @return the max health of the entity.
   * @throws PacketHandlingException if the method could not be invoked.
   */
  public float getMaxHealth() throws PacketHandlingException {
    try {
      Float maxHealth = WrappedEntityLiving.GET_MAX_HEALTH.prepare(this.wrapped);
      return maxHealth == null ? 0f : maxHealth;
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getMaxHealth");
    }
  }

  /**
   * Set the health of the entity.
   *
   * @param health the new health of the entity.
   * @throws PacketHandlingException if the method could not be invoked.
   */
  public void setHealth(float health) throws PacketHandlingException {
    try {
      WrappedEntityLiving.SET_HEALTH.invoke(this.wrapped, health);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setHealth");
    }
  }
}
