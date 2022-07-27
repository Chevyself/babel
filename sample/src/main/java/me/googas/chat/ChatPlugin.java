package me.googas.chat;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.YamlLanguage;
import me.googas.chat.api.commands.ChannelProvider;
import me.googas.chat.api.commands.ResultHandler;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.middleware.PermissionMiddleware;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.commands.providers.registry.ProvidersRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    ErrorHandler errors =
        ErrorHandler.setInstance(new ErrorHandler.LoggerErrorHandler(this.getLogger()));
    File lang = new File(this.getDataFolder(), "lang");
    // Load languages
    try {
      ResourceManager.getInstance().registerAll(this, YamlLanguage.load(this, lang, "en", "es"));
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
    super.onEnable();
  }

  @Override
  public void onDisable() {
    ResourceManager.getInstance().unregister(this);
    ErrorHandler.setDefaultInstance();
    super.onDisable();
  }
}
