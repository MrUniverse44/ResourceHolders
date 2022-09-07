package dev.mruniverse.resourceholders.groups;

public class EmptyGroup implements Group {

    public static final EmptyGroup EMPTY_INSTANCE = new EmptyGroup();

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getNegative() {
        return "";
    }
}
