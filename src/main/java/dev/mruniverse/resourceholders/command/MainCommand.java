package dev.mruniverse.resourceholders.command;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;

@Command(
        description = "Main Command of the plugin",
        shortDescription = "Plugin Main Command"
)
public class MainCommand implements SlimeCommand {

    private final ResourceHolders plugin;

    public MainCommand(ResourceHolders plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "rholders";
    }

    @Override
    public void execute(Sender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("resourceholders.admin")) {
            return;
        }
        sender.sendColoredMessage("&aPlugin has been reloaded!");
        plugin.reload();
    }
}
