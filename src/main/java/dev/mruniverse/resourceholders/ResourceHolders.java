package dev.mruniverse.resourceholders;

import dev.mruniverse.resourceholders.bstats.Metrics;
import dev.mruniverse.resourceholders.command.MainCommand;
import dev.mruniverse.resourceholders.command.RPCommand;
import dev.mruniverse.resourceholders.groups.GroupStorage;
import dev.mruniverse.resourceholders.groups.PermissionPlugin;
import dev.mruniverse.resourceholders.groups.supports.LuckPermsImpl;
import dev.mruniverse.resourceholders.groups.supports.None;
import dev.mruniverse.resourceholders.groups.supports.Vault;
import dev.mruniverse.resourceholders.loader.PluginLoader;
import dev.mruniverse.resourceholders.source.ResourcePack;
import dev.mruniverse.resourceholders.source.listener.PlaceholderListeners;
import dev.mruniverse.resourceholders.source.storage.PluginPlayer;

import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLogger;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceHolders extends JavaPlugin implements SlimePlugin<JavaPlugin> {

    private final Map<UUID, PluginPlayer> playerMap = new ConcurrentHashMap<>();
    private BaseSlimeLoader<JavaPlugin> loader;
    private SlimePluginInformation information;
    private PlaceholderListeners listener;
    private PermissionPlugin permissions;
    private ResourcePack resourcePack;
    private GroupStorage groups;
    private SlimeLogs logs;

    @Override
    public void onEnable() {

        this.information = new SlimePluginInformation(
                getServerType(),
                this
        );

        this.logs = SlimeLogger.createLogs(
                getServerType(),
                this
        );

        this.logs.getProperties().getPrefixes().changeMainText("ResourceHolders");

        this.loader = new PluginLoader(
                this
        );

        this.loader.setFiles(SlimeFile.class);

        this.loader.init();

        this.resourcePack = new ResourcePack(this);

        this.groups = new GroupStorage(this);

        if (hasPlugin("LuckPerms")) {
            Plugin plugin = getBukkitPlugin("LuckPerms");
            if (plugin != null) {
                String ver    = plugin.getDescription().getVersion();

                getLogs().info("&6Connected with LuckPerms v" + ver);
                permissions = new LuckPermsImpl(
                        ver
                );
            }
        } else {
            if (hasPlugin("Vault")) {
                RegisteredServiceProvider<Permission> provider = getServer().getServicesManager().getRegistration(Permission.class);
                Plugin plugin = getBukkitPlugin("Vault");
                if (plugin != null && provider != null) {
                    String ver    = plugin.getDescription().getVersion();

                    getLogs().info("&6Connected with Vault v" + ver);
                    permissions = new Vault(
                            provider.getProvider(),
                            ver
                    );
                }
            } else {
                permissions = new None();
            }
        }

        if (getConfigurationHandler(SlimeFile.RESOURCE_PACK).getStatus("resource-pack.custom-commands.enabled", true)) {
            for (String command : getConfigurationHandler(SlimeFile.RESOURCE_PACK).getStringList("resource-pack.custom-commands.commands")) {
                getCommands().register(
                        new RPCommand(this, command)
                );
            }
        }

        getCommands().register(
                new MainCommand(this)
        );

        this.listener = new PlaceholderListeners(this);

        this.listener.register();

        new Metrics(this, 16274);

        checkKeys();
    }

    public PluginPlayer getPlayer(Player player) {
        return playerMap.computeIfAbsent(player.getUniqueId(), v -> new PluginPlayer());
    }

    public boolean hasPlugin(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }

    @Nullable
    public Plugin getBukkitPlugin(String name) {
        return getServer().getPluginManager().getPlugin(name);
    }

    public PermissionPlugin getPermissions() {
        return permissions;
    }

    public GroupStorage getGroups() {
        return groups;
    }

    public void removePlayer(Player player) {
        playerMap.remove(player.getUniqueId());
        groups.getPlayerStorage().remove(player.getUniqueId());
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

    @Override
    public SlimePlatform getServerType() {
        return SlimePlatform.SPIGOT;
    }

    public ResourcePack getResourcePack() {
        return resourcePack;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    private void checkKeys() {
        for (String key : getConfigurationHandler(SlimeFile.PLACEHOLDERS).getContent("placeholders", false)) {
            if (key.equalsIgnoreCase("rank") || key.equalsIgnoreCase("rank_name")) {
                logs.error("'" + key + "' will not work because the plugin will use the group system");
                logs.error("Please rename your key and try another name to fix this issue");
            }
        }
    }

    @Override
    public void reload() {
        loader.reload();

        resourcePack.update();

        listener.update();

        getGroups().getController().loadGroups();

        checkKeys();
    }
}
