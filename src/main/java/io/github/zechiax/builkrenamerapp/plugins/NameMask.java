package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamePluginBase;

import java.util.ArrayList;

public class NameMask extends RenamePluginBase {
    private final String pattern = "[N]";

    @Override
    public String getName() {
        return "Name Mask";
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
            newName = replaceSubstring(newName, file.getBaseName(), index, index + pattern.length());
        }

        return newName;
    }
}
