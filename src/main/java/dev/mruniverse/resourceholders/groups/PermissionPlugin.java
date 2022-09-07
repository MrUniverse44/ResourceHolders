package dev.mruniverse.resourceholders.groups;

import org.bukkit.entity.Player;

public interface PermissionPlugin {
    /**
     * @param player - player to get primary group
     * @return group name
     */
    String getPrimaryGroup(final Player player);

    /**
     * @param player - player to get groups
     * @return list of all groups of the player
     */
    String[] getAllGroups(final Player player);

    /**
     * @return version of the permission plugin
     */
    String getVersion();

    /**
     * @return name of the permission plugin
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
