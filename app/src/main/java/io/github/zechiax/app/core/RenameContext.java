package io.github.zechiax.app.core;

import javafx.collections.ObservableList;

public record RenameContext(String currentName, FileToRename currentFile, int originalFilesIndex, ObservableList<FileToRename> originalFiles, String mask, CounterSettings counterSettings) {
}
