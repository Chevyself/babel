package me.googas.chat.wrappers;

import java.util.Optional;
import lombok.NonNull;
import me.googas.reflect.Wrapper;
import org.bukkit.boss.BarStyle;

public enum WrappedBarStyle implements Wrapper<BarStyle> {
  SOLID(BarStyle.SOLID),
  SEGMENTED_6(BarStyle.SEGMENTED_6),
  SEGMENTED_10(BarStyle.SEGMENTED_10),
  SEGMENTED_12(BarStyle.SEGMENTED_12),
  SEGMENTED_20(BarStyle.SEGMENTED_20);

  @NonNull private final BarStyle reference;

  WrappedBarStyle(@NonNull BarStyle reference) {
    this.reference = reference;
  }

  @Override
  public @NonNull Optional<BarStyle> get() {
    return Optional.of(reference);
  }

  @Override
  public @NonNull Wrapper<BarStyle> set(BarStyle barColor) {
    throw new UnsupportedOperationException("Cannot set values in enums");
  }
}
