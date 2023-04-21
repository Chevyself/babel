package com.github.chevyself.babel.packet.world;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.Wrapper;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.lang.reflect.InvocationTargetException;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.GameMode;

/** Wrapper for the nms GameMode class. */
public enum WrappedEnumGameMode implements Wrapper<Object> {
  SURVIVAL(GameMode.SURVIVAL),
  CREATIVE(GameMode.CREATIVE),
  ADVENTURE(GameMode.ADVENTURE),
  SPECTATOR(GameMode.SPECTATOR);

  @NonNull public static final WrappedClass<?> CLAZZ;

  static {
    if (Versions.BUKKIT >= 10) {
      CLAZZ = Versions.wrapNmsClassByName("world.level", "EnumGamemode");
    } else {
      CLAZZ = Versions.wrapNmsClassByName("WorldSettings$EnumGamemode");
    }
  }

  public static final WrappedMethod<?> VALUE_OF =
      WrappedEnumGameMode.CLAZZ.getMethod("valueOf", String.class);
  @NonNull @Getter private final GameMode bukkit;

  WrappedEnumGameMode(@NonNull GameMode bukkit) {
    this.bukkit = bukkit;
  }

  /**
   * Get the gamemode from the bukkit enum.
   *
   * @param invoke the bukkit enum
   * @return the gamemode
   * @throws IllegalArgumentException if the gamemode could not be found
   */
  @NonNull
  public static WrappedEnumGameMode valueOf(Object invoke) {
    if (invoke instanceof Enum) {
      return WrappedEnumGameMode.valueOf(((Enum<?>) invoke).name());
    }
    throw new IllegalArgumentException("Cannot get gamemode from " + invoke);
  }

  /**
   * Get the gamemode from the bukkit enum.
   *
   * @param gameMode the bukkit enum
   * @return the gamemode
   * @throws IllegalArgumentException if the gamemode could not be found
   */
  @NonNull
  public static WrappedEnumGameMode valueOf(@NonNull GameMode gameMode) {
    for (WrappedEnumGameMode mode : WrappedEnumGameMode.values()) {
      if (mode.getBukkit().equals(gameMode)) {
        return mode;
      }
    }
    throw new IllegalArgumentException("Cannot match " + gameMode);
  }

  @Override
  public Object getWrapped() {
    try {
      return WrappedEnumGameMode.VALUE_OF.invoke(null, this.name());
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new IllegalStateException("Could not get value of gamemode " + this, e);
    }
  }
}
