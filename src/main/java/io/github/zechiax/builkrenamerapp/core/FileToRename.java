package io.github.zechiax.builkrenamerapp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class FileToRename extends File {

    public FileToRename(File file) {
        super(file.getAbsolutePath());
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static Collection<? extends FileToRename> convertToFilesToRename(Collection<File> files) {
        ArrayList<FileToRename> filesToRename = new ArrayList<>();
        for (File file : files) {
            filesToRename.add(new FileToRename(file));
        }
        return filesToRename;
    }
}
