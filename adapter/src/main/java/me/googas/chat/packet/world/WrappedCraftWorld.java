package me.googas.chat.packet.world;

import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import me.googas.chat.exceptions.PacketHandlingException;
import me.googas.chat.packet.Packet;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import org.bukkit.World;

public class WrappedCraftWorld extends SimpleWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CRAFT_WORLD =
      WrappedClass.forName("org.bukkit.craftbukkit." + Packet.NMS + ".CraftWorld");

  @NonNull
  private static final WrappedMethod<?> GET_HANDLE =
      WrappedCraftWorld.CRAFT_WORLD.getMethod("getHandle");

  WrappedCraftWorld(Object reference) {
    super(reference);
  }

  @NonNull
  public static WrappedCraftWorld of(@NonNull World world) {
    return new WrappedCraftWorld(world);
  }

  @NonNull
  public WrappedWorldServer getHandle() throws PacketHandlingException {
    try {
      return new WrappedWorldServer(WrappedCraftWorld.GET_HANDLE.invoke(this.reference));
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new PacketHandlingException("Could not get the entity from player", e);
    }
  }
}
