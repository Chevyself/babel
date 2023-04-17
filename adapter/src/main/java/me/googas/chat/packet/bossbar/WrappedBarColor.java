package me.googas.chat.packet.bossbar;

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
  @NonNull private final BarColor wrapped;

  WrappedBarColor(@NonNull BarColor wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public BarColor getWrapped() {
    return this.wrapped;
  }
}
