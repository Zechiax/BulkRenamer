package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamingException;

import java.util.regex.Pattern;

public class ExtensionRangeMask extends RangePluginBase {
    private final String pattern = "(\\[E(\\d+)-(\\d+)?\\])";
    private final Pattern patternRegex = Pattern.compile(pattern);
    @Override
    public String getName() {
        return "Extension Range Mask";
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

            var extension = context.currentFile().getExtension();

            var start = Integer.parseInt(group.split("-")[0].replace("[E", ""));
            var endString = group.split("-")[1].replace("]", "");

            if (endString.isEmpty()) {
                endString = String.valueOf(extension.length());
            }

            var end = Integer.parseInt(endString);

            if (!isStartEndValid(start, end, extension.length())) {
                throw new RenamingException("Start and end are not valid");
            }

            newName = replaceSubstring(newName, extension.substring(start - 1, end), indices[0], indices[0] + group.length());
        }

        return newName;
    }
}
