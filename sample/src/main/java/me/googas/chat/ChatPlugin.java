package me.googas.chat;

import java.io.IOException;
import java.util.logging.Level;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lang.YamlLanguage;
import me.googas.chat.api.bossbar.WitherTask;
import me.googas.chat.api.commands.ChannelProvider;
import me.googas.chat.api.commands.ResultHandler;
import me.googas.chat.api.util.Versions;
import me.googas.chat.debug.Debugger;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.middleware.PermissionMiddleware;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.commands.providers.registry.ProvidersRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    Debugger errors = Debugger.setInstance(new Debugger.LoggerDebugger(this.getLogger()));
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
    Debugger.setDefaultInstance();
    super.onDisable();
  }
}
