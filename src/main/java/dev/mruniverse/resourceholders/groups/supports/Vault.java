package dev.mruniverse.resourceholders.groups.supports;

import dev.mruniverse.resourceholders.groups.PermissionPlugin;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class Vault implements PermissionPlugin {

    //permission plugin
    private final Permission permission;

    //vault version
    private final String vaultVersion;

    /**
     * Constructs new instance with given parameters
     * @param permission permission plugin implementation
     * @param vaultVersion vault version
     */
    public Vault(Permission permission, String vaultVersion) {
        this.permission = permission;
        this.vaultVersion = vaultVersion;
    }


    public String getPrimaryGroup(Player player) {
        if (getName().equals("SuperPerms")) return "<null>";
        return permission.getPrimaryGroup(player);
    }


    public String[] getAllGroups(Player player) {
        if (getName().equals("SuperPerms")) return new String[] {"<null>"};
        return permission.getPlayerGroups(player);
    }


    public String getName() {
        return permission.getName();
    }


    public String getVersion() {
        return "Vault " + vaultVersion;
    }
}
