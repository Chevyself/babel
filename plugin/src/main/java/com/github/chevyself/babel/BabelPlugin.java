package com.github.chevyself.babel;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.bossbar.WitherTask;
import com.github.chevyself.babel.api.commands.ChannelProvider;
import com.github.chevyself.babel.api.commands.ResultHandler;
import com.github.chevyself.babel.api.commands.SoundProvider;
import com.github.chevyself.babel.api.commands.TextProvider;
import com.github.chevyself.babel.api.commands.WrappedSoundCategoryProvider;
import com.github.chevyself.babel.api.lang.YamlLanguage;
import com.github.chevyself.babel.commands.BabelCommand;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.starbox.bukkit.CommandManager;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import com.github.chevyself.starbox.bukkit.messages.MessagesProvider;
import com.github.chevyself.starbox.bukkit.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.bukkit.providers.registry.BukkitProvidersRegistry;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class of the babel plugin. */
public class BabelPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    Debugger debugger = new Debugger(this.getLogger());
    Debugger.setInstance(debugger);
    // Load languages
    try {
      ResourceManager.getInstance()
          .registerAll(this, YamlLanguage.load(this, this.getDataFolder(), "info.yml", "lang/en"));
    } catch (IOException e) {
      debugger.getLogger().log(Level.SEVERE, "Failed to create 'lang' directory", e);
    }
    // Load commands
    MessagesProvider messages = new BukkitMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new BukkitProvidersRegistry(messages)
            .addProviders(
                new ChannelProvider(),
                new SoundProvider(),
                new TextProvider(),
                new WrappedSoundCategoryProvider());
    CommandManager manager =
        new CommandManager(this, registry, messages)
            .addGlobalMiddlewares(new PermissionMiddleware(), new ResultHandler())
            .registerPlugin();
    manager.registerAll(BabelCommand.getCommands(manager));

    if (Versions.getBukkit().getMajor() == 8) {
      Bukkit.getScheduler().runTaskTimer(this, new WitherTask(), 0, 2);
    }

    super.onEnable();
  }

  @Override
  public void onDisable() {
    ResourceManager.getInstance().unregister(this);
    Debugger.setInstance(new Debugger());
    super.onDisable();
  }
}
