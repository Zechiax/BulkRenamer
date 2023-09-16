package io.github.zechiax.app.core;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
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

    public String getBaseName() {
        return FilenameUtils.getBaseName(this.getAbsolutePath());
    }

    public BasicFileAttributes getFileAttributeView() throws IOException {
        return Files.readAttributes(this.toPath(), BasicFileAttributes.class);
    }

    public void renameTo(String newName) throws IOException {
        Files.move(this.toPath(), this.toPath().resolveSibling(newName));
    }

    public String getExtension() {
        return FilenameUtils.getExtension(this.getAbsolutePath());
    }
}
