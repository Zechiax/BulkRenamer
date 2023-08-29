package io.github.zechiax.builkrenamerapp.core;

import io.github.zechiax.builkrenamerapp.plugins.ExtensionMask;
import io.github.zechiax.builkrenamerapp.plugins.NameMask;
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
        plugins.add(new ExtensionMask());
    }

    public ArrayList<String> GetRenamedFiles(String mask) {
        logger.info("Renaming files with mask: " + mask);
        var currentNames = new ArrayList<String>();
        for (var ignored : files) {
            currentNames.add(mask);
        }

        logger.info("Active plugins:" + plugins.size());
        for (var plugin : plugins) {
            logger.info("Using plugin: " + plugin.getName());
            var context = new RenameContext(files, currentNames, mask);

            // If the plugin extends RenamePluginBase, set the context
            if (plugin instanceof RenamePluginBase) {
                ((RenamePluginBase) plugin).setContext(context);
            }

            currentNames = plugin.rename(context);
        }

        return currentNames;
    }

    public String getRenamedFile(String mask, FileToRename file) {
        var currentNames = new ArrayList<String>();
        currentNames.add(mask);

        ObservableList<FileToRename> oneFile = FXCollections.observableArrayList();
        oneFile.add(file);
        currentNames.add(mask);

        for(var plugin: plugins) {
            var context = new RenameContext(oneFile, currentNames, mask);

            if (plugin instanceof RenamePluginBase) {
                ((RenamePluginBase) plugin).setContext(context);
            }

            currentNames = plugin.rename(context);
        }

        return currentNames.get(0);
    }
}
