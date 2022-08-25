package dev.mruniverse.resourceholders.source.listener;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.source.storage.ResourcePackStorage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackListener implements Listener {

    private final ResourceHolders plugin;

    public ResourcePackListener(ResourceHolders plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void resourcePack(PlayerResourcePackStatusEvent event) {
        final Player player = event.getPlayer();

        ResourcePackStorage storage = plugin.getResourcePack().getStorage();

        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        switch (status) {
            case DECLINED:
                if (storage.hasMessages()) {
                    send(player, storage.getMessage(ResourcePackStorage.Message.DECLINED));
                }

                plugin.getPlayer(player).setEnabled(false);
                break;
            case ACCEPTED:
                if (storage.hasMessages()) {
                    send(player, storage.getMessage(ResourcePackStorage.Message.DOWNLOADING));
                }

                plugin.getPlayer(player).setEnabled(false);
                break;
            case FAILED_DOWNLOAD:
                if (storage.hasMessages()) {
                    send(player, storage.getMessage(ResourcePackStorage.Message.FAILED));
                }

                plugin.getPlayer(player).setEnabled(false);
                break;
            case SUCCESSFULLY_LOADED:
                if (storage.hasMessages()) {
                    send(player, storage.getMessage(ResourcePackStorage.Message.SUCCESSFULLY_DOWNLOADED));
                }
                plugin.getPlayer(player).setEnabled(true);
                break;
        }
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        plugin.getResourcePack().verify(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.removePlayer(event.getPlayer());
    }

    private void send(final Player player, String[] messages) {
        for(String message : messages) {
            player.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',message)
            );
        }
    }
}

