package io.github.zechiax.builkrenamerapp.core;

import io.github.zechiax.builkrenamerapp.plugins.ExtensionMask;
import io.github.zechiax.builkrenamerapp.plugins.ExtensionRangeMask;
import io.github.zechiax.builkrenamerapp.plugins.NameMask;
import io.github.zechiax.builkrenamerapp.plugins.NameRangeMask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.logging.Logger;

public class RenameManager {
    private static final Logger logger = Logger.getLogger(RenameManager.class.getName());
    private final ArrayList<RenamePlugin> plugins;
    private final ObservableList<FileToRename> files;
    public RenameManager(ObservableList<FileToRename> files) {
        this.files = files;
        plugins = new ArrayList<>();
        LoadDefaultPlugins();
    }

    public void AddPlugin(RenamePlugin plugin) {
        plugins.add(plugin);
    }

    public void RemovePlugin(RenamePlugin plugin) {
        plugins.remove(plugin);
    }

    public ArrayList<RenamePlugin> GetPlugins() {
        return plugins;
    }

    public void LoadDefaultPlugins() {
        plugins.add(new NameMask());
        plugins.add(new NameRangeMask());
        plugins.add(new ExtensionMask());
        plugins.add(new ExtensionRangeMask());
    }

    public ArrayList<String> getRenamedFiles(String mask) {
        logger.info("Renaming files with mask: " + mask);
        var currentNames = new ArrayList<String>();
        for (var ignored : files) {
            currentNames.add(mask);
        }

        logger.info("Active plugins:" + plugins.size());
        for (var plugin : plugins) {
            logger.info("Plugin: " + plugin.getName());
            for (var i = 0; i < files.size(); i++) {
                var name = getRenamedFile(currentNames.get(i), files.get(i));
                currentNames.set(i, name);
            }
        }

        return currentNames;
    }

    public String getRenamedFile(String mask, FileToRename file) {

        // Index of the file in the list
        var index = files.indexOf(file);

        if (index == -1) {
            throw new IllegalArgumentException("File not found in list");
        }

        var newName = mask;

        for (var plugin : plugins) {
            var context = new RenameContext(newName, file, index, files, mask);

            if (plugin instanceof RenamePluginBase) {
                ((RenamePluginBase) plugin).setContext(context);
            }

            try {
                newName = plugin.rename(context);
            } catch (RenamingException e) {
                logger.warning("Plugin " + plugin.getName() + " threw an exception: " + e.getMessage());
                newName = "<Error!>";
            }

        }

        return newName;
    }
}
