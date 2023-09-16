package io.github.zechiax.api;

import javafx.collections.ObservableList;

public class PluginContext {
    private final String currentName;
    private final FileToRename currentFile;
    private final int originalFilesIndex;
    private final ObservableList<FileToRename> originalFiles;
    private final String mask;
    private final CounterSettings counterSettings;

    public PluginContext(
        String currentName,
        FileToRename currentFile,
        int originalFilesIndex,
        ObservableList<FileToRename> originalFiles,
        String mask,
        CounterSettings counterSettings
    ) {
        this.currentName = currentName;
        this.currentFile = currentFile;
        this.originalFilesIndex = originalFilesIndex;
        this.originalFiles = originalFiles;
        this.mask = mask;
        this.counterSettings = counterSettings;
    }

    public String getCurrentName() {
        return currentName;
    }

    public FileToRename getCurrentFile() {
        return currentFile;
    }

    public int getOriginalFilesIndex() {
        return originalFilesIndex;
    }

    public ObservableList<FileToRename> getOriginalFiles() {
        return originalFiles;
    }

    public String getMask() {
        return mask;
    }

    public CounterSettings getCounterSettings() {
        return counterSettings;
    }
}
