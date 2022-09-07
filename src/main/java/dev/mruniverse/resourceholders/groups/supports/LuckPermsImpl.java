package dev.mruniverse.resourceholders.groups.supports;

import dev.mruniverse.resourceholders.groups.GroupStorage;
import dev.mruniverse.resourceholders.groups.PermissionPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class LuckPermsImpl implements PermissionPlugin {

    private static final String UPDATE_MESSAGE = "Please install LuckPerms5 or higher";

    private final String version;

    /**
     * Constructs new instance with given parameter
     * @param version - luckPerms version
     */
    public LuckPermsImpl(String version) {
        this.version = version;
    }


    public String getPrimaryGroup(Player p) {
        if (version.startsWith("4")) {
            return UPDATE_MESSAGE;
        }

        LuckPerms api = getAPI();

        if (api == null) {
            return GroupStorage.DEFAULT_GROUP;
        }

        User user = api.getUserManager().getUser(p.getUniqueId());

        if (user == null) {
            return GroupStorage.DEFAULT_GROUP;
        }

        return user.getPrimaryGroup();
    }


    public String[] getAllGroups(Player p) {
        if (version.startsWith("4")) {
            return new String[]{UPDATE_MESSAGE};
        }
        LuckPerms api = getAPI();
        if (api == null) {
            return new String[]{GroupStorage.DEFAULT_GROUP};
        }
        User user = api.getUserManager().getUser(p.getUniqueId());

        if (user == null) {
            return new String[] {GroupStorage.DEFAULT_GROUP};
        }

        return user.getNodes().stream().filter(NodeType.INHERITANCE::matches).map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName).distinct().toArray(String[]::new);
    }

    private LuckPerms getAPI() {
        try {
            return LuckPermsProvider.get();
        } catch (Throwable e) {
            Bukkit.getLogger().log(Level.WARNING,"LuckPerms v" + version + " threw an exception when retrieving API instance: " + e.getMessage());
            Bukkit.getLogger().info("Just a side note: LuckPerms is installed, otherwise server would not say it is. LuckPerms is declared as soft-dependency and all code runs at onEnable or later, constructor is unused.");
            return null;
        }
    }


    public String getVersion() {
        return version;
    }
}

