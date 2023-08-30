package io.github.zechiax.builkrenamerapp.core;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public record RenameContext(String currentName, FileToRename currentFile, int originalFilesIndex, ObservableList<FileToRename> originalFiles, String mask, CounterSettings counterSettings) {
}
