package me.googas.chat.packet.entity;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.world.WrappedWorld;
import me.googas.chat.packet.world.WrappedWorldServer;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;

public class WrappedEntityWither extends WrappedEntityLiving {

  @NonNull
  private static final WrappedClass<?> ENTITY_WITHER =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".EntityWither");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      ENTITY_WITHER.getConstructor(WrappedWorld.CLAZZ.getClazz());

  @NonNull
  private static final WrappedMethod<?> SET_LOCATION =
      ENTITY_WITHER.getMethod(
          "setLocation", double.class, double.class, double.class, float.class, float.class);

  @NonNull
  private static final WrappedMethod<?> SET_INVISIBLE =
      ENTITY_WITHER.getMethod("setInvisible", boolean.class);

  @NonNull
  private static final WrappedMethod<?> SET_CUSTOM_NAME =
      ENTITY_WITHER.getMethod("setCustomName", String.class);

  @NonNull
  private static final WrappedMethod<?> SET_HEALTH =
      ENTITY_WITHER.getMethod("setHealth", float.class);

  @NonNull
  private static final WrappedMethod<Integer> GET_ID = ENTITY_WITHER.getMethod(int.class, "getId");

  @NonNull
  private static final WrappedMethod<Float> GET_MAX_HEALTH =
      ENTITY_WITHER.getMethod(float.class, "getMaxHealth");

  @NonNull
  private static final WrappedMethod<?> GET_DATA_WATCHER =
      ENTITY_WITHER.getMethod("getDataWatcher");

  WrappedEntityWither(Object reference) {
    super(reference);
  }

  public static WrappedEntityWither construct(@NonNull WrappedWorldServer server)
      throws PacketHandlingException {
    try {
      return new WrappedEntityWither(CONSTRUCTOR.invoke(server.get().orElse(null)));
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not create EntityEnderDragon", e);
    }
  }

  public void setLocation(double x, double y, double z, float yaw, float pitch)
      throws PacketHandlingException {
    try {
      SET_LOCATION.invoke(this.reference, x, y, z, yaw, pitch);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setLocation", e);
    }
  }

  public void setInvisible(boolean visible) throws PacketHandlingException {
    try {
      SET_INVISIBLE.invoke(this.reference, visible);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setInvisible", e);
    }
  }

  public void setCustomName(String name) throws PacketHandlingException {
    try {
      SET_CUSTOM_NAME.invoke(this.reference, name);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setCustomName", e);
    }
  }

  public float getMaxHealth() throws PacketHandlingException {
    try {
      return GET_MAX_HEALTH.prepare(this.reference);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getMaxHealth");
    }
  }

  public void setHealth(float health) throws PacketHandlingException {
    try {
      SET_HEALTH.invoke(this.reference, health);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#setHealth");
    }
  }

  public int getId() throws PacketHandlingException {
    try {
      return GET_ID.prepare(this.reference);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getId");
    }
  }

  public Object getDataWatcher() throws PacketHandlingException {
    try {
      return GET_DATA_WATCHER.invoke(this.reference);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getDataWatcher");
    }
  }

  public WrappedDataWatcher getWrappedDataWatcher() throws PacketHandlingException {
    try {
      return new WrappedDataWatcher(GET_DATA_WATCHER.invoke(this.reference));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not invoke Entity#getDataWatcher");
    }
  }
}
