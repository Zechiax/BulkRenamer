package io.github.zechiax.app.core;

import io.github.zechiax.api.*;
import io.github.zechiax.app.plugins.*;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RenameManager {
    private static final Logger logger = Logger.getLogger(RenameManager.class.getName());
    private final ArrayList<RenamePluginBase> plugins;
    private final ObservableList<FileToRename> files;
    private final CounterSettings counterSettings = CounterSettings.getDefault();
    private String renameFind = "";
    private String renameReplace = "";

    public void setFindReplace(String findString, String replacementString) {
        renameFind = findString;
        renameReplace = replacementString;
    }

    public RenameManager(ObservableList<FileToRename> files) {
        this.files = files;
        plugins = new ArrayList<>();
        LoadDefaultPlugins();
    }

    public ArrayList<RenamePluginBase> GetPlugins() {
        return plugins;
    }

    public void LoadDefaultPlugins() {
        plugins.add(new NameMask());
        plugins.add(new NameRangeMask());
        plugins.add(new ExtensionMask());
        plugins.add(new ExtensionRangeMask());
        plugins.add(new CounterMask());
        plugins.add(new DateMask());
    }

    public void setCounterSettings(Integer start, Integer step, Integer padding) {
        counterSettings.setStart(start);
        counterSettings.setStep(step);
        counterSettings.setPadding(padding);
    }

    public CounterSettings getCounterSettings() {
        return counterSettings;
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
            var context = new PluginContext(newName, file, index, files, mask, counterSettings);

            plugin.setContext(context);


            try {
                newName = plugin.rename();
            } catch (RenamingException e) {
                logger.warning("Plugin " + plugin.getName() + " threw an exception: " + e.getMessage());
                newName = "<Error!>";
            }

        }

        // After applying all plugins, find and replace
        newName = findAndReplace(newName);

        return newName;
    }

    private String findAndReplace(String name) {
        return name.replace(renameFind, renameReplace);
    }

    public void renameFiles(String mask) throws IOException {
        var names = getRenamedFiles(mask);

        for (var i = 0; i < files.size(); i++) {
            var file = files.get(i);
            var name = names.get(i);

            file.renameTo(name);
        }
    }
}
