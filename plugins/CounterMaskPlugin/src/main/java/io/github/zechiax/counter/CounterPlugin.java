package io.github.zechiax.counter;

import io.github.zechiax.api.PluginContext;
import io.github.zechiax.api.RenamePluginBase;
import org.pf4j.Extension;
import org.pf4j.Plugin;

public class CounterPlugin extends Plugin {
    @Extension
    public static class CounterMask extends RenamePluginBase {
    private static final String pattern = "[C]";

    @Override
    public String getName() {
        return "Counter Mask";
    }

    private String getStringCountFromContext(PluginContext context) {
        var counterSettings = context.getCounterSettings();

        var position = context.getOriginalFilesIndex();

        var step = counterSettings.getStep();
        var start = counterSettings.getStart();

        var number = start + (position * step);

        var padding = counterSettings.getPadding();

        // Here we compute the string that will be added to the name, with the correct padding
        return String.format("%0" + padding + "d", number);
    }

    @Override
    public String rename() {
        var indices = getPatternIndices(pattern, context.getCurrentName());

        var counter = getStringCountFromContext(context);

        var newName = context.getCurrentName();

        for (var index : indices) {
            newName = replaceSubstring(context.getCurrentName(), counter, index, index + pattern.length());
        }

        return newName;
    }
}
}
