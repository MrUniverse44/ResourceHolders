package dev.mruniverse.resourceholders.loader;

import dev.mruniverse.slimelib.SlimeFiles;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.file.storage.DefaultFileStorage;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.utils.SlimeHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLoader extends BaseSlimeLoader<JavaPlugin> {

    public PluginLoader(SlimePlugin<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        if (getFiles() != null) {
            getFiles().init();
        }
    }

    public <O extends Enum<O> & SlimeFiles> void setFiles(Class<O> clazz) {
        fileStorage(
                new DefaultFileStorage(
                        getPlugin()
                ).setEnums(
                        SlimeHelper.process(
                                clazz
                        )
                )
        );
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
