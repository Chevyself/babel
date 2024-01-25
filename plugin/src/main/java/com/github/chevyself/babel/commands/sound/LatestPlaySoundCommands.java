package com.github.chevyself.babel.commands.sound;

import com.github.chevyself.babel.api.channels.Channel;
import com.github.chevyself.babel.packet.sound.WrappedSoundCategory;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.common.CommandPermission;
import org.bukkit.Sound;

public class LatestPlaySoundCommands {

  @CommandPermission("babel.sound")
  @Command(
      aliases = {"playSound", "sound"},
      description = "Play a sound")
  public void playSound(
      Channel channel,
      @Required(name = "sound", description = "The sound to play") Sound sound,
      @Required(name = "category", description = "The category of the sound")
          WrappedSoundCategory category,
      @Required(name = "volume", description = "The volume of the sound", suggestions = "1")
          double volume,
      @Required(name = "pitch", description = "The pitch of the sound", suggestions = "1")
          double pitch) {
    channel.playSound(sound, category, (float) volume, (float) pitch);
  }
}
