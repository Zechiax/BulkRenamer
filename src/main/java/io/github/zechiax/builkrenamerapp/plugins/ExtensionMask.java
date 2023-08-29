package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamePluginBase;

import java.util.ArrayList;

public class ExtensionMask extends RenamePluginBase {
    private final String pattern = "[E]";

    @Override
    public String getName() {
        return "Extension Mask";
    }

    @Override
    public ArrayList<String> rename(RenameContext context) {
        var indices = getPatternIndices(pattern);

        if (indices.length == 0) {
            return context.currentNames();
        }

        var files = context.originalFiles();

        var newNames = new ArrayList<String>(context.originalFiles().size());

        for (int i = 0; i < files.size(); i++) {
            var file = files.get(i);
            var newName = context.mask();
            for (var index : indices) {
                // On the index, we replace the pattern with the file name
                newName = replaceSubstring(newName, file.getExtension(), index, index + pattern.length());
            }
            newNames.add(i, newName);
        }

        return newNames;
    }
}
