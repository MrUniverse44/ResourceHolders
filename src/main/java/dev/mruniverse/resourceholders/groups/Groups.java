package dev.mruniverse.resourceholders.groups;

import dev.mruniverse.resourceholders.source.storage.PluginStorage;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.SlimeFile;

public class Groups {
    private final PluginStorage<String, Group> ranks = PluginStorage.initAsConcurrentHash();
    private final ResourceHolders plugin;

    public Groups(ResourceHolders plugin) {
        this.plugin = plugin;
    }

    public void loadGroups() {
        ConfigurationHandler handler = plugin.getConfigurationHandler(SlimeFile.GROUPS);

        ranks.clear();

        plugin.getLogs().info("&6GROUPS &8| &fLoading Groups placeholders");
        for (String name : handler.getContent("groups", false)) {

            GroupImpl group = new GroupImpl(
                    handler,
                    name
            );

            ranks.add(
                    name,
                    group
            );

            plugin.getLogs().info("&6GROUPS &8| &eGROUP &8| &e" + name + "&f has been loaded");
        }
        if(ranks.size() != 1) {
            plugin.getLogs().info("&6GROUPS &8| &f" + ranks.size() + " groups has been loaded.");
        } else {
            plugin.getLogs().info("&6GROUPS &8| &f" + ranks.size() + " group has been loaded.");
        }
    }


    public PluginStorage<String, Group> getGroups() {
        return ranks;
    }
}
