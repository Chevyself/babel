package me.googas.chat.packet.world;

import java.lang.reflect.InvocationTargetException;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.reflect.Wrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import org.bukkit.GameMode;

public enum WrappedEnumGameMode implements Wrapper<Object> {
  SURVIVAL(GameMode.SURVIVAL),
  CREATIVE(GameMode.CREATIVE),
  ADVENTURE(GameMode.ADVENTURE),
  SPECTATOR(GameMode.SPECTATOR);

  @NonNull
  public static final WrappedClass<?> CLAZZ =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".WorldSettings$EnumGamemode");

  public static final WrappedMethod<?> VALUE_OF = CLAZZ.getMethod("valueOf", String.class);
  @NonNull @Getter private final GameMode bukkit;

  WrappedEnumGameMode(@NonNull GameMode bukkit) {
    this.bukkit = bukkit;
  }

  @NonNull
  public static WrappedEnumGameMode valueOf(Object invoke) {
    if (invoke instanceof Enum) {
      return valueOf(((Enum<?>) invoke).name());
    }
    throw new IllegalArgumentException("Cannot get gamemode from " + invoke);
  }

  @NonNull
  public static WrappedEnumGameMode valueOf(@NonNull GameMode gameMode) {
    for (WrappedEnumGameMode mode : values()) {
      if (mode.getBukkit().equals(gameMode)) {
        return mode;
      }
    }
    throw new IllegalArgumentException("Cannot match " + gameMode);
  }

  @Override
  public Object getWrapped() {
    try {
      return VALUE_OF.invoke(null, this.name());
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new IllegalStateException("Could not get value of gamemode " + this, e);
    }
  }
}
