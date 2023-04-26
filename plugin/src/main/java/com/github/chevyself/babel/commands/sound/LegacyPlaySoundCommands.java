package com.github.chevyself.babel.commands.sound;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import org.bukkit.Sound;

public class LegacyPlaySoundCommands {

  @Command(
      aliases = {"playSound", "sound"},
      description = "Play a sound",
      permission = "babel.sound")
  public void playSound(
      Channel channel,
      @Required(name = "sound", description = "The sound to play") Sound sound,
      @Required(name = "volume", description = "The volume of the sound", suggestions = "1")
          double volume,
      @Required(name = "pitch", description = "The pitch of the sound", suggestions = "1")
          double pitch) {
    channel.playSound(sound, (float) volume, (float) pitch);
  }
}
