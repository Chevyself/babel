package com.github.chevyself.babel;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.bossbar.WitherTask;
import com.github.chevyself.babel.api.commands.ChannelProvider;
import com.github.chevyself.babel.api.commands.ResultHandler;
import com.github.chevyself.babel.api.lang.YamlLanguage;
import com.github.chevyself.babel.debug.ErrorHandler;
import com.github.chevyself.babel.util.Versions;
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

public class ChatPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    ErrorHandler errors =
        ErrorHandler.setInstance(new ErrorHandler.LoggerErrorHandler(this.getLogger()));
    // Load languages
    try {
      ResourceManager.getInstance()
          .registerAll(this, YamlLanguage.load(this, this.getDataFolder(), "lang/en", "lang/es"));
    } catch (IOException e) {
      errors.handle(Level.SEVERE, "Failed to create 'lang' directory", e);
    }
    // Load commands
    MessagesProvider messages = new BukkitMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new BukkitProvidersRegistry(messages)
            .addProviders(new ChannelProvider(), new SoundProvider());
    new CommandManager(this, registry, messages)
        .addGlobalMiddlewares(new PermissionMiddleware(), new ResultHandler())
        .parseAndRegisterAll(new SampleCommands())
        .registerPlugin();

    if (Versions.BUKKIT == 8) {
      Bukkit.getScheduler().runTaskTimer(this, new WitherTask(), 0, 2);
    }

    super.onEnable();
  }

  @Override
  public void onDisable() {
    ResourceManager.getInstance().unregister(this);
    ErrorHandler.setDefaultInstance();
    super.onDisable();
  }
}
