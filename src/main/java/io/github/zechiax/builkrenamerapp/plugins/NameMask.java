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
    public ArrayList<String> rename(RenameContext context) {
        if (!isPatternInMask(pattern)) {
            return context.currentNames();
        }

        var files = context.originalFiles();

        var newNames = new ArrayList<String>(files.size());

        for (int i = 0; i < files.size(); i++) {
            var file = files.get(i);
            var newName = context.currentNames().get(i);

            var indices = getPatternIndices(pattern, newName);

            for (var index : indices) {
                // On the index, we replace the pattern with the file name
                newName = replaceSubstring(newName, file.getBaseName(), index, index + pattern.length());
            }
            newNames.add(i, newName);
        }

        return newNames;
    }
}
