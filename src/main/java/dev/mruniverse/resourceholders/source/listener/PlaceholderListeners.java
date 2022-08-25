package dev.mruniverse.resourceholders.source.listener;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.resourceholders.SlimeFile;
import dev.mruniverse.resourceholders.source.storage.PluginPlayer;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderListeners extends PlaceholderExpansion {
    private ConfigurationHandler configuration;

    private final ResourceHolders main;

    public PlaceholderListeners(ResourceHolders main) {
        this.main = main;

        load();
    }

    private void load() {
        this.configuration = main.getConfigurationHandler(SlimeFile.PLACEHOLDERS);
    }

    public void update() {
        load();
    }

    /**
     * The placeholder identifier of this expansion. May not contain {@literal %},
     * {@literal {}} or _
     *
     * @return placeholder identifier that is associated with this expansion
     */
    @Override
    public @NotNull String getIdentifier() {
        return "resourceholders";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public @NotNull String getAuthor() {
        return "JustJustin";
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "No player online for identifier: " + params + " registered!";
        }

        PluginPlayer pluginPlayer = main.getPlayer(player);

        if (pluginPlayer.isEnabled()) {
            for (String key : configuration.getContent("placeholders", false)) {
                if (params.equalsIgnoreCase(key)) {
                    return configuration.getString("placeholders." + key + ".value", "&cNot found!");
                }
            }
        } else {
            for (String key : configuration.getContent("placeholders", false)) {
                if (params.equalsIgnoreCase(key)) {
                    return configuration.getString("placeholders." + key + ".negative", "&cNot found!");
                }
            }
        }
        return "No placeholder identifier: placeholders." + params + " registered!";
    }
}
