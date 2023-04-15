package me.googas.chat.api.tab.entries;

import lombok.NonNull;
import me.googas.chat.api.text.Text;
import me.googas.chat.api.tab.TabSlot;
import me.googas.chat.packet.entity.player.Skin;
import me.googas.chat.packet.world.WrappedEnumGameMode;

/** An entry that does not display anything. It does not have text and has a default gray skin. */
public class EmptyTabEntry implements TabEntry {

  @Override
  public @NonNull Text getDisplay(@NonNull TabSlot slot) {
    return Text.of("");
  }

  @Override
  public @NonNull WrappedEnumGameMode getGamemode(@NonNull TabSlot slot) {
    return WrappedEnumGameMode.SURVIVAL;
  }

  @Override
  public Skin getSkin() {
    return new Skin(
        "ewogICJ0aW1lc3RhbXAiIDogMTYwOTM4MDg1OTU2OSwKICAicHJvZmlsZUlkIiA6ICI1MjM4OWQ3NTNhNTM0NmZkYTYyZWI5YTIxNmE1NGY1MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJXb2xmZ2FtZTE2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIyZTEwY2I0MjkwMDA4OWFhNDM4Yzk3ZDMyZmYyNmYwNDIxYWIyNWI5ODg4YzQ4NGNiNmZjMjJiOWVmYTJjMDUiCiAgICB9CiAgfQp9",
        "JYRqveH64fyBd2hrKIr9ET4+WzrI/AULYc+fwTSHsaTPUYgc+QFI1gGpNFOutFXG9qJ0uv7ckvwfBOjV8Hatc9G4yvS5v+n8sOIZg5eir9rGWqH5R4o62xDUMDOpuzAWTGdHpfjCWE9/rZV24vrJQuP6+5jdCY/OH3tc2CwSTLt6JBYG9ioETFMlnm0aS8ORgAxMURS2fz3ZRLRMU3I3Fumy2r2NCFhphDXZpjYyV2N5W6EXHDXSc/B0pN7mKwM1NmCFEj58OeJCXIPuWL4bBltbaxmO7b28+ZoYX4QTOpV0gYpjsG2taxgbehUrmf80cpHe6v7k5+6dQriThKQB0l9iP5VDEfBnzuJ8aJZpYkPz0tjhKVBJ9fEfJofkuY1PWWChr/9JGnxh1qsXTUwddSogfIavAGDCewwM5QXR9liLP4Hgce951Ro2N5Fo+3nS36YIAz56m8cwnbc/xkh6cuHT6MtYjYgZ+YhqVWJSXN1XiRHfxRz3X9+heMC+YgRSrVAf6g9m7Vg7UsBs8ybYkgecgHqpswZlE3boC8X1PyGFjAiQ/O1Kmkq7c42prqt1b3WyB1UiehMR37JlUw1UF4y/f9zSwTFHtzV3MZ7zS5x0igfzvXj0wyknCsddHGy6e8wybI+23WpouoYN/6ccfGwogUVs6ZSpd6mumHQp9TE=");
  }

  @Override
  public int getPing(@NonNull TabSlot slot) {
    return 99;
  }

  @Override
  public boolean canBeReplaced(@NonNull TabEntry entry) {
    return true;
  }

  @Override
  public int compareTo(@NonNull TabEntry o) {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof EmptyTabEntry;
  }
}
