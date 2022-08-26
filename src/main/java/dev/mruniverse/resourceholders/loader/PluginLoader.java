package dev.mruniverse.resourceholders.loader;

import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimeStorage;
import dev.mruniverse.slimelib.file.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLoader extends BaseSlimeLoader<JavaPlugin> {

    public PluginLoader(SlimePlugin<JavaPlugin> plugin, InputManager inputManager) {
        super(plugin);

        super.storage(new SlimeStorage(
                plugin.getServerType(),
                plugin.getLogs(),
                inputManager
        ));
    }

    @Override
    public void init() {
        if (getFiles() != null) {
            getFiles().init();
        }
    }

    @Override
    public void shutdown() {
        getCommands().unregister();
    }

    @Override
    public void reload() {
        getFiles().reloadFiles();
    }


}
