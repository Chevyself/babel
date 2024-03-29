package com.github.chevyself.babel.packet.bossbar;

import com.github.chevyself.reflect.Wrapper;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.boss.BarStyle;

/** Wraps the {@link BarStyle} enum. */
public enum WrappedBarStyle implements Wrapper<BarStyle> {
  SOLID(BarStyle.SOLID),
  SEGMENTED_6(BarStyle.SEGMENTED_6),
  SEGMENTED_10(BarStyle.SEGMENTED_10),
  SEGMENTED_12(BarStyle.SEGMENTED_12),
  SEGMENTED_20(BarStyle.SEGMENTED_20);

  @Getter @NonNull private final BarStyle wrapped;

  WrappedBarStyle(@NonNull BarStyle wrapped) {
    this.wrapped = wrapped;
  }
}
