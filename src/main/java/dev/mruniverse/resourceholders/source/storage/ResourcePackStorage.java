package dev.mruniverse.resourceholders.source.storage;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.SlimeFile;

import dev.mruniverse.resourceholders.source.listener.ResourcePackListener;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;

public class ResourcePackStorage {
    private final ResourceHolders plugin;

    private String[] downloading;

    private String[] successfully;

    private boolean hasMessages;

    private String[] declined;

    private String[] failed;

    public ResourcePackStorage(ResourceHolders plugin) {
        this.plugin = plugin;

        load(
                plugin.getConfigurationHandler(SlimeFile.RESOURCE_PACK)
        );

        ResourcePackListener listener = new ResourcePackListener(plugin);

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);

    }

    private void load(ConfigurationHandler settings) {
        hasMessages = settings.getBoolean("messages.toggle");

        plugin.getLogs().info("Resource Pack system now is enabled");

        downloading = settings.getString(
                "messages.downloading",
                "<nl>&a&lDownloading resource pack<nl>"
        ).split("<nl>");
        successfully = settings.getString(
                "messages.successfully-downloaded",
                "<nl>&a&lThe resource pack has been installed correctly!<nl>"
        ).split("<nl>");
        declined = settings.getString(
                "messages.declined",
                "<nl>&c&lYou don't installed the resource pack, if you want install the resource pack use: &b&l/download<nl>"
        ).split("<nl>");
        failed = settings.getString("messages.failed",
                "<nl>&c&lCan't download the resource pack, if you want try again use: &f&l/download<nl>"
        ).split("<nl>");
    }

    public void update(ConfigurationHandler configuration) {
        load(configuration);
    }

    public boolean hasMessages() {
        return hasMessages;
    }

    public String[] getMessage(Message message) {
        switch (message) {
            case FAILED:
                return failed;
            case DECLINED:
                return declined;
            case DOWNLOADING:
                return downloading;
            default:
            case SUCCESSFULLY_DOWNLOADED:
                return successfully;
        }
    }

    public enum Message {
        DECLINED,
        DOWNLOADING,
        SUCCESSFULLY_DOWNLOADED,
        FAILED
    }


}
