package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamePluginBase;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ExtensionMask extends RenamePluginBase {
    private static final Logger logger = Logger.getLogger(ExtensionMask.class.getName());
    private final String pattern = "[E]";

    @Override
    public String getName() {
        return "Extension Mask";
    }

    @Override
    public String rename(RenameContext context) {
        if (!isPatternInMask(pattern)) {
            return context.currentName();
        }

        var file = context.currentFile();
        var newName = context.currentName();

        var indices = getPatternIndices(pattern, newName);

        for (var index : indices) {
            // On the index, we replace the pattern with the file name
            newName = replaceSubstring(newName, file.getExtension(), index, index + pattern.length());
        }

        return newName;
    }
}
