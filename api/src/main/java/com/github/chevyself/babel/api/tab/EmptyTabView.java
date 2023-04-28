package com.github.chevyself.babel.api.tab;

import com.github.chevyself.babel.api.tab.entries.TabEntry;
import com.github.chevyself.reflect.debug.Debugger;
import java.util.Collection;
import java.util.function.Supplier;
import lombok.NonNull;

/**
 * An empty implementation of {@link TabView}. It should not be used as it will log a warning when
 * any of its methods are called.
 *
 * <p>It is used as a not null value for {@link TabView}
 */
public class EmptyTabView implements TabView {
  @Override
  public void clear() {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#clear");
  }

  @Override
  public void initialize() {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#initialize");
  }

  @Override
  public void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry) {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#set");
  }

  @Override
  public @NonNull TabSize getSize() {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#getSize");
    return TabSize.ONE;
  }

  @Override
  public boolean add(@NonNull TabEntry entry) {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#add");
    return false;
  }

  @Override
  public boolean add(@NonNull Collection<TabEntry> entries) {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#add");
    return false;
  }

  @Override
  public boolean remove(
      @NonNull Supplier<TabEntry> replacement, @NonNull Collection<? extends TabEntry> entries) {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#remove");
    return false;
  }

  @Override
  public void sort() {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#sort");
  }

  @Override
  public void destroy() {
    Debugger.getInstance().getLogger().fine("Use of EmptyTabView#destroy");
  }
}
