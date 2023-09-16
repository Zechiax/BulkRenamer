package io.github.zechiax.api;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.pf4j.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public abstract class RenamePluginBase extends Plugin {
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String name = this.getClass().getSimpleName();
    protected PluginContext context;

    public String getName() {
        return name;
    }

    public void setContext(PluginContext context) {
        this.context = context;
    }

    public String rename() throws RenamingException {
        return context.getCurrentName();
    }

    public PluginContext getContext() {
        return context;
    }

    protected Integer[] getPatternIndices(String pattern) {
        return getPatternIndices(pattern, context.getMask());
    }

    protected Integer[] getPatternIndices(String pattern, @NotNull String mask) {
        var firstIndex = mask.indexOf(pattern);

        if (firstIndex == -1) {
            return new Integer[0];
        }

        var indices = new ArrayList<Integer>();

        indices.add(firstIndex);

        var lastIndex = firstIndex;

        while (lastIndex != -1) {
            lastIndex = mask.indexOf(pattern, lastIndex + 1);
            if (lastIndex != -1) {
                indices.add(lastIndex);
            }
        }

        var indicesArray = indices.toArray(new Integer[0]);
        ArrayUtils.reverse(indicesArray);

        return indicesArray;
    }

    protected boolean isPatternInMask(String pattern) {
        return context.getMask().contains(pattern);
    }

    protected String replaceSubstring(@NotNull String string, String replacement, int start, int end) {
        return string.substring(0, start) + replacement + string.substring(end);
    }

    protected static ArrayList<String> getGroupsFromMatcher(Matcher matcher, boolean reverseList) {
        var groups = new ArrayList<String>();

        while (matcher.find()) {
            groups.add(matcher.group());
        }

        if (reverseList) {
            var groupsArray = groups.toArray(new String[0]);
            ArrayUtils.reverse(groupsArray);
            groups = new ArrayList<>(groupsArray.length);
            Collections.addAll(groups, groupsArray);
        }

        return groups;
    }
}
