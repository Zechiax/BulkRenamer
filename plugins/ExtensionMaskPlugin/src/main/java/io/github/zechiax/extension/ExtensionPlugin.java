package io.github.zechiax.extension;

import io.github.zechiax.api.RangePluginBase;
import io.github.zechiax.api.RenamePluginBase;
import io.github.zechiax.api.RenamingException;
import org.pf4j.Extension;
import org.pf4j.Plugin;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ExtensionPlugin extends Plugin {

    @Extension
    public static class ExtensionMask extends RenamePluginBase {
        private static final Logger logger = Logger.getLogger(ExtensionMask.class.getName());
        private final String pattern = "[E]";

        @Override
        public String getName() {
            return "Extension Mask";
        }

        @Override
        public String rename() {
            if (!isPatternInMask(pattern)) {
                return context.getCurrentName();
            }

            var file = context.getCurrentFile();
            var newName = context.getCurrentName();

            var indices = getPatternIndices(pattern, newName);

            for (var index : indices) {
                // On the index, we replace the pattern with the file name
                newName = replaceSubstring(newName, file.getExtension(), index, index + pattern.length());
            }

            return newName;
        }
    }

    @Extension
    public class ExtensionRangeMask extends RangePluginBase {
        private final String pattern = "(\\[E(\\d+)-(\\d+)?\\])";
        private final Pattern patternRegex = Pattern.compile(pattern);

        @Override
        public String getName() {
            return "Extension Range Mask";
        }

        @Override
        public String rename() throws RenamingException {
            // We match the pattern to the current name
            var matcher = patternRegex.matcher(context.getCurrentName());

            var groups = getGroupsFromMatcher(matcher, true);

            if (groups.isEmpty()) {
                return context.getCurrentName();
            }

            var newName = context.getCurrentName();
            for (var group : groups) {
                var indices = getPatternIndices(group, newName);

                var extension = context.getCurrentFile().getExtension();

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
}
