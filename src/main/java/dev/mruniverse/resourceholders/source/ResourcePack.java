package dev.mruniverse.resourceholders.source;

import com.comphenix.protocol.ProtocolLibrary;
import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.SlimeFile;
import dev.mruniverse.resourceholders.source.storage.ResourcePackStorage;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.source.SlimeSource;
import dev.mruniverse.slimelib.source.player.SlimePlayer;
import org.bukkit.entity.Player;

public class ResourcePack {

    private final ResourcePackStorage storage;
    private final ResourceHolders plugin;
    private boolean PERMISSION_OPTION;
    private boolean PROTOCOL_OPTION;
    private boolean INCLUDE_HASH;
    private String PERMISSION;
    private int MAX_PROTOCOL;
    private int MIN_PROTOCOL;
    private byte[] BYTES;
    private String URL;

    public ResourcePack(ResourceHolders plugin) {
        this.storage = new ResourcePackStorage(plugin);
        this.plugin = plugin;
        update();
    }

    public void update() {
        ConfigurationHandler settings = plugin.getConfigurationHandler(SlimeFile.RESOURCE_PACK);

        storage.update(settings);

        PERMISSION_OPTION = settings.getStatus("resource-pack.install-conditions.permission.enabled", false);
        PROTOCOL_OPTION = settings.getStatus("resource-pack.install-conditions.protocol.enabled", true);
        INCLUDE_HASH = settings.getStatus("resource-pack.include-hash.enabled", false);

        MIN_PROTOCOL = settings.getInt("resource-pack.install-conditions.protocol.min", 477);
        MAX_PROTOCOL = settings.getInt("resource-pack.install-conditions.protocol.max", -1);

        PERMISSION = settings.getString("resource-pack.install-conditions.permission.value", "resourceholder.install");

        BYTES = settings.getString("resource-pack.include-hash.hash", "nothing here for now").getBytes();

        URL = settings.getString("resource-pack.url", "here the url");
    }

    public void verify(Player player) {
        if (URL.equalsIgnoreCase("here the url")) {
            plugin.getLogs().info("Plugin is not configured yet! Can't verify " + player.getName());
            return;
        }
        if (PERMISSION_OPTION && !player.hasPermission(PERMISSION)) {
            return;
        }

        int protocol = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);


        if (PROTOCOL_OPTION) {

            if (MIN_PROTOCOL != -1) {
                if (MAX_PROTOCOL != -1) {
                    if (protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
                        send(player);
                    }
                } else {
                    if (protocol >= MIN_PROTOCOL) {
                        send(player);
                    }
                }
            } else {
                if (MAX_PROTOCOL != -1) {
                    if (protocol <= MAX_PROTOCOL) {
                        send(player);
                    }
                } else {
                    send(player);
                }
            }
        }
    }

    public void send(Player player) {
        if (INCLUDE_HASH) {
            player.setResourcePack(URL, BYTES);
        } else {
            player.setResourcePack(URL);
        }
    }

    public void verify(SlimeSource<?> sender) {
        if (sender.isPlayer()) {
            verify(
                    ((SlimePlayer)sender).get()
            );
        }
    }

    public ResourcePackStorage getStorage() {
        return storage;
    }
}
