package me.googas.chat.packet.bossbar;

import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.Wrapper;
import org.bukkit.boss.BarStyle;

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
