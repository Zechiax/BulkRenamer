package io.github.zechiax.builkrenamerapp.core;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
    // List of files
    private final ArrayList<File> files;
    // Current directory
    private File currentDirectory;

    public FileManager() {
        this.files = new ArrayList<>();
    }
}
