package com.github.chevyself.babel.packet.sound;

import com.github.chevyself.reflect.Wrapper;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.SoundCategory;

/** Class to wrap {@link SoundCategory} to not crash when older versions cannot use it. */
// TODO change this to enum
public enum WrappedSoundCategory implements Wrapper<SoundCategory> {
  MASTER(SoundCategory.MASTER),
  MUSIC(SoundCategory.MUSIC),
  RECORDS(SoundCategory.RECORDS),
  WEATHER(SoundCategory.WEATHER),
  BLOCKS(SoundCategory.BLOCKS),
  HOSTILE(SoundCategory.HOSTILE),
  NEUTRAL(SoundCategory.NEUTRAL),
  PLAYERS(SoundCategory.PLAYERS),
  AMBIENT(SoundCategory.AMBIENT),
  VOICE(SoundCategory.VOICE);

  @Getter @NonNull private final SoundCategory wrapped;

  WrappedSoundCategory(@NonNull SoundCategory wrapped) {
    this.wrapped = wrapped;
  }
}
