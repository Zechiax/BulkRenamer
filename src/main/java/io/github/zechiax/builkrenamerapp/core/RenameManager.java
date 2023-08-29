package io.github.zechiax.builkrenamerapp.core;

import io.github.zechiax.builkrenamerapp.plugins.NameMask;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class RenameManager {
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
    }

    public ArrayList<String> GetRenamedFiles(String mask) {
        var currentNames = new ArrayList<String>();
        for (var file : files) {
            currentNames.add(file.getName());
        }

        for (var plugin : plugins) {
            var context = new RenameContext(files, currentNames, mask);

            // If the plugin extends RenamePluginBase, set the context
            if (plugin instanceof RenamePluginBase) {
                ((RenamePluginBase) plugin).setContext(context);
            }

            currentNames = plugin.rename(context);
        }

        return currentNames;
    }
}
