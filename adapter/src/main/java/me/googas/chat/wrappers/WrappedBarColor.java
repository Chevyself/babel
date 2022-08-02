package me.googas.chat.wrappers;

import java.util.Optional;
import lombok.NonNull;
import me.googas.reflect.Wrapper;
import org.bukkit.boss.BarColor;

public enum WrappedBarColor implements Wrapper<BarColor> {
  PINK(BarColor.PINK),
  BLUE(BarColor.BLUE),
  RED(BarColor.RED),
  GREEN(BarColor.GREEN),
  YELLOW(BarColor.YELLOW),
  PURPLE(BarColor.PURPLE),
  WHITE(BarColor.WHITE);
  @NonNull private final BarColor reference;

  WrappedBarColor(@NonNull BarColor reference) {
    this.reference = reference;
  }

  @Override
  public @NonNull Optional<BarColor> get() {
    return Optional.of(reference);
  }

  @Override
  public @NonNull Wrapper<BarColor> set(BarColor barColor) {
    throw new UnsupportedOperationException("Cannot set values in enums");
  }
}
