package com.github.chevyself.babel.adapters.tab.views;

import com.github.chevyself.babel.adapters.tab.TabCoordinate;
import com.github.chevyself.babel.adapters.tab.TabEntry;
import com.github.chevyself.babel.adapters.tab.TabSize;
import com.github.chevyself.babel.adapters.tab.TabView;
import com.github.chevyself.babel.debug.ErrorHandler;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.logging.Level;
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
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#clear");
  }

  @Override
  public void initialize() {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#initialize");
  }

  @Override
  public void set(@NonNull TabCoordinate coordinate, @NonNull TabEntry entry) {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#set");
  }

  @Override
  public @NonNull TabSize getSize() {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#getSize");
    return TabSize.ONE;
  }

  @Override
  public boolean add(@NonNull TabEntry entry) {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#add");
    return false;
  }

  @Override
  public boolean add(@NonNull Collection<TabEntry> entries) {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#add");
    return false;
  }

  @Override
  public boolean remove(
      @NonNull Supplier<TabEntry> replacement, @NonNull Collection<? extends TabEntry> entries) {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#remove");
    return false;
  }

  @Override
  public void sort() {
    ErrorHandler.getInstance().handle(Level.FINE, "Use of EmptyTabView#sort");
  }
}
