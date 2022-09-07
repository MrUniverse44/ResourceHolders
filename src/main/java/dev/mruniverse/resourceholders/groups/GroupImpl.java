package dev.mruniverse.resourceholders.groups;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;

public class GroupImpl implements Group {

    private final String name;
    private final String prefix;
    private final String negative;

    public GroupImpl(ConfigurationHandler groups, String name) {
        this.negative = groups.getString(TextDecoration.LEGACY, "groups." + name + ".negative", "");
        this.prefix   = groups.getString(TextDecoration.LEGACY, "groups." + name + ".value", "");
        this.name     = name;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNegative() {
        return negative;
    }
}
