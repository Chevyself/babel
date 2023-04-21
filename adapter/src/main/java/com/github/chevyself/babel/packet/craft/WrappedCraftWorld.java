package com.github.chevyself.babel.packet.craft;

import com.github.chevyself.babel.exceptions.PacketHandlingException;
import com.github.chevyself.babel.packet.Packet;
import com.github.chevyself.babel.packet.ReflectWrapper;
import com.github.chevyself.babel.packet.world.WrappedWorldServer;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import org.bukkit.World;

public class WrappedCraftWorld extends ReflectWrapper {

  @NonNull
  private static final WrappedClass<?> CRAFT_WORLD =
      WrappedClass.forName("org.bukkit.craftbukkit." + Packet.NMS + ".CraftWorld");

  @NonNull
  private static final WrappedMethod<?> GET_HANDLE =
      WrappedCraftWorld.CRAFT_WORLD.getMethod("getHandle");

  private WrappedCraftWorld(Object reference) {
    super(reference);
  }

  @Override
  public Class<?> getReflectClass() {
    return CRAFT_WORLD.getWrapped();
  }

  @NonNull
  public static WrappedCraftWorld of(@NonNull World world) {
    return new WrappedCraftWorld(world);
  }

  @NonNull
  public WrappedWorldServer getHandle() throws PacketHandlingException {
    try {
      return new WrappedWorldServer(WrappedCraftWorld.GET_HANDLE.invoke(this.wrapped));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the entity from player", e);
    }
  }
}
