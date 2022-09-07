package dev.mruniverse.resourceholders.groups.supports;

import dev.mruniverse.resourceholders.groups.PermissionPlugin;
import org.bukkit.entity.Player;

/**
 * An instance of PermissionPlugin to be used when none is found
 */
public class None implements PermissionPlugin {


    public String getPrimaryGroup(Player player) {
        return "<null>";
    }


    public String[] getAllGroups(Player player) {
        return new String[]{"<null>"};
    }


    public String getName() {
        return "Unknown/None";
    }


    public String getVersion() {
        return "-";
    }
}