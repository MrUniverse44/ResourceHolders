package dev.mruniverse.resourceholders.groups;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.SlimeFile;
import dev.mruniverse.resourceholders.source.storage.PluginStorage;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GroupStorage {
    private final PluginStorage<UUID, Group> playerStorage = PluginStorage.initAsConcurrentHash();
    private final List<String> primaryGroupFindingList;
    public static final String DEFAULT_GROUP = "NONE";
    private final boolean groupsByPermissions;
    private final boolean usePrimaryGroup;
    private final ResourceHolders plugin;
    private final Groups controller;

    public GroupStorage(ResourceHolders plugin) {
        this.plugin = plugin;

        this.controller = new Groups(plugin);


        this.controller.loadGroups();

        ConfigurationHandler settings = plugin.getConfigurationHandler(SlimeFile.GROUP_SETTINGS);

        this.usePrimaryGroup = settings.getBoolean("settings.groups.use-primary-group", true);
        this.groupsByPermissions = settings.getBoolean("settings.groups.assign-groups-by-permissions", false);
        this.primaryGroupFindingList = new ArrayList<>();

        List<String> groups = settings.getStringList("settings.groups.primary-group-finding-list");

        if(!groups.isEmpty()) {
            for (Object group : groups) {
                primaryGroupFindingList.add(group.toString());
            }
        } else {
            for (Object group : Arrays.asList("Owner", "Admin", "Helper", "default")) {
                primaryGroupFindingList.add(group.toString());
            }
        }
    }

    public Groups getController() {
        return controller;
    }

    public Group getGroup(Player player) {
        if (playerStorage.contains(player.getUniqueId())) {
            return playerStorage.get(player.getUniqueId());
        }
        String name = detectPermissionGroup(player);
        Group group = getController().getGroups().get(name);
        if (group == null) {
            group = EmptyGroup.EMPTY_INSTANCE;
        }
        playerStorage.add(
                player.getUniqueId(),
                group
        );
        return group;
    }

    public PluginStorage<UUID, Group> getPlayerStorage() {
        return playerStorage;
    }

    public String detectPermissionGroup(Player player) {
        if (isGroupsByPermissions()) {
            return getByPermission(player);
        }
        if (isUsePrimaryGroup()) {
            return getByPrimary(player);
        }
        return getFromList(player);
    }

    public String getByPrimary(Player player) {
        try {
            return plugin.getPermissions().getPrimaryGroup(player);
        } catch (Throwable e) {
            e.printStackTrace();
            return DEFAULT_GROUP;
        }
    }

    public String getFromList(Player player) {
        try {
            String[] playerGroups = plugin.getPermissions().getAllGroups(player);
            for (String groupFromList : primaryGroupFindingList) {
                for (String playerGroup : playerGroups) {
                    if (playerGroup.equalsIgnoreCase(groupFromList)) {
                        return playerGroup;
                    }
                }
            }
            return playerGroups[0];
        } catch (Exception exception) {
            plugin.getLogs().error(exception);
            return DEFAULT_GROUP;
        }
    }

    public String getByPermission(Player player) {
        for (Object group : primaryGroupFindingList) {
            if (player.hasPermission("resourceholders.group." + group)) {
                return String.valueOf(group);
            }
        }
        plugin.getLogs().error("Player " + player.getName() + " does not have any group permission while assign-groups-by-permissions is enabled! Did you forget to add his group to primary-group-finding-list?");
        return DEFAULT_GROUP;
    }

    public boolean isGroupsByPermissions() {
        return groupsByPermissions;
    }

    public boolean isUsePrimaryGroup() {
        return usePrimaryGroup;
    }
}

