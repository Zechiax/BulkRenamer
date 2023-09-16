package io.github.zechiax.app.plugins;

import io.github.zechiax.app.core.RenameContext;
import io.github.zechiax.app.core.RenamePluginBase;

public class CounterMask extends RenamePluginBase {
    private static final String pattern = "[C]";

    @Override
    public String getName() {
        return "Counter Mask";
    }

    private String getStringCountFromContext(RenameContext context) {
        var counterSettings = context.counterSettings();

        var position = context.originalFilesIndex();

        var step = counterSettings.getStep();
        var start = counterSettings.getStart();

        var number = start + (position * step);

        var padding = counterSettings.getPadding();

        // Here we compute the string that will be added to the name, with the correct padding
        return String.format("%0" + padding + "d", number);
    }

    @Override
    public String rename(RenameContext context) {
        var indices = getPatternIndices(pattern, context.currentName());

        var counter = getStringCountFromContext(context);

        var newName = context.currentName();

        for (var index : indices) {
            newName = replaceSubstring(context.currentName(), counter, index, index + pattern.length());
        }

        return newName;
    }
}
