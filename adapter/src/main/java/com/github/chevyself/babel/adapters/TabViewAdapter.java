package com.github.chevyself.babel.adapters;

import com.github.chevyself.babel.adapters.tab.views.PlayerTabView;
import java.util.UUID;
import lombok.NonNull;

public interface TabViewAdapter {

  @NonNull
  PlayerTabView getTabView(@NonNull UUID uuid);
}
