package me.googas.chat.api.tab.entries;

import lombok.NonNull;

public class BlankTabEntry extends EmptyTabEntry {

  @Override
  public boolean canBeReplaced(@NonNull TabEntry entry) {
    return false;
  }
}
