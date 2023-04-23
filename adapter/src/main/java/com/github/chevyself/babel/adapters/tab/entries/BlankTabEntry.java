package com.github.chevyself.babel.adapters.tab.entries;

import com.github.chevyself.babel.adapters.tab.TabEntry;
import lombok.NonNull;

/** This is the same as a {@link EmptyTabEntry} but it cannot be replaced by any other entry. */
public class BlankTabEntry extends EmptyTabEntry {

  @Override
  public boolean canBeReplaced(@NonNull TabEntry entry) {
    return false;
  }
}
