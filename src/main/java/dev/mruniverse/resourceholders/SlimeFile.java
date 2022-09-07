package dev.mruniverse.resourceholders;

import dev.mruniverse.slimelib.SlimeFiles;
import dev.mruniverse.slimelib.SlimePlatform;

public enum SlimeFile implements SlimeFiles {
    GROUPS("groups.yml", "groups", "groups.yml"),
    GROUP_SETTINGS("settings.yml", "groups", "group_settings.yml"),
    PLACEHOLDERS("placeholders.yml"),
    RESOURCE_PACK("resource-pack.yml");

    private final boolean differentFolder;

    private final String file;

    private final String folder;

    private final String resource;

    SlimeFile(String file) {
        this.file = file;
        this.resource = "/" + file;
        this.differentFolder = false;
        this.folder = "";
    }

    SlimeFile(String file, String folder) {
        this.file = file;
        this.resource = "/" + folder + "/" + file;
        this.differentFolder = true;
        this.folder = folder;
    }

    SlimeFile(String file,String folder,String resource) {
        this.file = file;
        this.resource = resource;
        this.differentFolder = true;
        this.folder = folder;
    }

    SlimeFile(String file,String folderOrResource,boolean isResource) {
        this.file = file;
        if(isResource) {
            this.resource = folderOrResource;
            this.folder = "";
            this.differentFolder = false;
        } else {
            this.resource = file;
            this.folder = folderOrResource;
            this.differentFolder = true;
        }
    }

    @Override
    public String getFileName() {
        return this.file;
    }

    @Override
    public String getFolderName() {
        return this.folder;
    }

    @Override
    public String getResourceFileName(SlimePlatform platform) {
        return this.resource;
    }

    @Override
    public boolean isInDifferentFolder() {
        return this.differentFolder;
    }
}
