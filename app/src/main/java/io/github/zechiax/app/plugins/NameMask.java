package io.github.zechiax.app.plugins;

import io.github.zechiax.app.core.PluginContext;
import io.github.zechiax.app.core.RenamePluginBase;

public class NameMask extends RenamePluginBase {
    private final String pattern = "[N]";

    @Override
    public String getName() {
        return "Name Mask";
    }

    @Override
    public String rename(PluginContext context) {
        if (!isPatternInMask(pattern)) {
            return context.getCurrentName();
        }

        var file = context.getCurrentFile();
        var newName = context.getCurrentName();

        var indices = getPatternIndices(pattern, newName);

        for (var index : indices) {
            // On the index, we replace the pattern with the file name
            newName = replaceSubstring(newName, file.getBaseName(), index, index + pattern.length());
        }

        return newName;
    }
}
