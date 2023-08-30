package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamingException;

import java.util.regex.Pattern;

public class NameRangeMask extends RangePluginBase {
    private final String pattern = "(\\[N(\\d+)-(\\d+)\\])";
    private final Pattern patternRegex = Pattern.compile(pattern);
    @Override
    public String getName() {
        return "Name Range Mask";
    }

    @Override
    public String rename(RenameContext context) throws RenamingException {
        // We match the pattern to the current name
        var matcher = patternRegex.matcher(context.currentName());

        var groups = getGroupsFromMatcher(matcher, true);

        if (groups.isEmpty()) {
            return context.currentName();
        }

        var newName = context.currentName();
        for (var group : groups) {
            var indices = getPatternIndices(group, newName);

            var baseName = context.currentFile().getBaseName();

            var start = Integer.parseInt(group.split("-")[0].replace("[N", ""));
            var end = Integer.parseInt(group.split("-")[1].replace("]", ""));

            if (!isStartEndValid(start, end, baseName.length())) {
                throw new RenamingException("Start and end are not valid");
            }

            newName = replaceSubstring(newName, baseName.substring(start, end), indices[0], indices[0] + group.length());
        }

        return newName;
    }
}
