package dev.mruniverse.resourceholders;

import dev.mruniverse.resourceholders.bstats.Metrics;
import dev.mruniverse.resourceholders.command.RPCommand;
import dev.mruniverse.resourceholders.source.ResourcePack;
import dev.mruniverse.resourceholders.source.listener.PlaceholderListeners;
import dev.mruniverse.resourceholders.source.storage.PluginPlayer;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.file.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLogger;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceHolders extends JavaPlugin implements SlimePlugin<JavaPlugin> {

    private final Map<UUID, PluginPlayer> playerMap = new ConcurrentHashMap<>();
    private BaseSlimeLoader<JavaPlugin> loader;
    private SlimePluginInformation information;
    private PlaceholderListeners listener;
    private ResourcePack resourcePack;
    private SlimeLogs logs;

    @Override
    public void onEnable() {
        information = new SlimePluginInformation(
                getServerType(),
                this
        );
        loader = new DefaultSlimeLoader<>(
                this,
                InputManager.getAutomatically()
        );
        logs = SlimeLogger.createLogs(
                getServerType(),
                this
        );

        logs.getProperties().getPrefixes().changeMainText("ResourceHolders");

        loader.setFiles(SlimeFile.class);

        loader.init();

        resourcePack = new ResourcePack(this);

        if (getConfigurationHandler(SlimeFile.RESOURCE_PACK).getStatus("resource-pack.custom-commands.enabled", true)) {
            for (String command : getConfigurationHandler(SlimeFile.RESOURCE_PACK).getStringList("resource-pack.custom-commands.commands")) {
                getLoader().getCommands().register(
                        new RPCommand(this, command)
                );
            }
        }

        listener = new PlaceholderListeners(this);

        listener.register();

        new Metrics(this, 16274);
    }

    public PluginPlayer getPlayer(Player player) {
        return playerMap.computeIfAbsent(player.getUniqueId(), v -> new PluginPlayer());
    }

    public void removePlayer(Player player) {
        playerMap.remove(player.getUniqueId());
    }

    @Override
    public SlimePluginInformation getPluginInformation() {
        return information;
    }

    @Override
    public BaseSlimeLoader<JavaPlugin> getLoader() {
        return loader;
    }

    @Override
    public SlimeLogs getLogs() {
        return logs;
    }

    public ResourcePack getResourcePack() {
        return resourcePack;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public void reload() {
        loader.reload();

        resourcePack.update();

        listener.update();
    }
}
