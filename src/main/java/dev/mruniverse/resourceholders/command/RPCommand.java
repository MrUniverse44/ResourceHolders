package dev.mruniverse.resourceholders.command;

import dev.mruniverse.resourceholders.ResourceHolders;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;

@Command(
        description = "Resource Pack Install Command",
        shortDescription = "RP Install CMD",
        usage = "/<command>"
)
public class RPCommand implements SlimeCommand {
    private final ResourceHolders plugin;
    private final String command;

    public RPCommand(ResourceHolders plugin, String command) {
        this.command = command;
        this.plugin  = plugin;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void execute(Sender sender, String commandLabel, String[] args) {
        plugin.getResourcePack().verify(sender);

    }
}
