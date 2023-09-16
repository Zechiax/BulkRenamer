package io.github.zechiax.app.plugins;

import io.github.zechiax.api.PluginContext;
import io.github.zechiax.app.core.RenamingException;

import java.util.regex.Pattern;

public class NameRangeMask extends RangePluginBase {
    private final String pattern = "(\\[N(\\d+)-(\\d+)?\\])";
    private final Pattern patternRegex = Pattern.compile(pattern);
    @Override
    public String getName() {
        return "Name Range Mask";
    }

    @Override
    public String rename(PluginContext context) throws RenamingException {
        // We match the pattern to the current name
        var matcher = patternRegex.matcher(context.getCurrentName());

        var groups = getGroupsFromMatcher(matcher, true);

        if (groups.isEmpty()) {
            return context.getCurrentName();
        }

        var newName = context.getCurrentName();
        for (var group : groups) {
            var indices = getPatternIndices(group, newName);

            var baseName = context.getCurrentFile().getBaseName();

            var start = Integer.parseInt(group.split("-")[0].replace("[N", ""));
            var endString = group.split("-")[1].replace("]", "");

            if (endString.isEmpty()) {
                endString = String.valueOf(baseName.length());
            }

            var end = Integer.parseInt(endString);

            if (!isStartEndValid(start, end, baseName.length())) {
                throw new RenamingException("Start and end are not valid");
            }

            newName = replaceSubstring(newName, baseName.substring(start - 1, end), indices[0], indices[0] + group.length());
        }

        return newName;
    }
}
