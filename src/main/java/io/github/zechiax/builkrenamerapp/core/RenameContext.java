package io.github.zechiax.builkrenamerapp.core;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public record RenameContext(ObservableList<FileToRename> originalFiles, ArrayList<String> currentNames, String mask) {
}
