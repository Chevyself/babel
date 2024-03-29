package com.github.chevyself.babel;

import com.github.chevyself.babel.api.ResourceManager;
import com.github.chevyself.babel.api.bossbar.WitherTask;
import com.github.chevyself.babel.api.commands.ChannelProvider;
import com.github.chevyself.babel.api.commands.ResultHandler;
import com.github.chevyself.babel.api.commands.SoundProvider;
import com.github.chevyself.babel.api.commands.TextProvider;
import com.github.chevyself.babel.api.commands.WrappedBarColorProvider;
import com.github.chevyself.babel.api.commands.WrappedBarStyleProvider;
import com.github.chevyself.babel.api.commands.WrappedSoundCategoryProvider;
import com.github.chevyself.babel.api.dependencies.viaversion.ViaVersionSoft;
import com.github.chevyself.babel.api.lang.YamlLanguage;
import com.github.chevyself.babel.api.listeners.PlayerChannelListener;
import com.github.chevyself.babel.commands.BabelCommand;
import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.debug.Debugger;
import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
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
    CommandManager<CommandContext, BukkitCommand> manager =
        new CommandManagerBuilder<>(new BukkitAdapter(this, true)).build();
    manager
        .getProvidersRegistry()
        .addProviders(
            new ChannelProvider(),
            new SoundProvider(),
            new TextProvider(),
            new WrappedBarColorProvider(),
            new WrappedBarStyleProvider(),
            new WrappedSoundCategoryProvider());
    manager.getMiddlewareRegistry().addGlobalMiddleware(new ResultHandler());
    manager.registerAll(BabelCommand.getCommands(manager));

    if (Versions.getBukkit().getMajor() == 8) {
      Bukkit.getScheduler().runTaskTimer(this, new WitherTask(), 0, 2);
    }

    // Registers required listeners
    if (ViaVersionSoft.isEnabled()) {
      // Protocol listeners
      ViaVersionSoft.registerProtocol(this);
    }
    // Player listeners
    Bukkit.getServer().getPluginManager().registerEvents(new PlayerChannelListener(), this);

    super.onEnable();
  }

  @Override
  public void onDisable() {
    ResourceManager.getInstance().unregister(this);
    HandlerList.unregisterAll(this);
    Debugger.setInstance(new Debugger());
    super.onDisable();
  }
}
