package io.github.zechiax.builkrenamerapp.core;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BinaryOperator;

public abstract class RenamePluginBase implements RenamePlugin {
    private final String name = this.getClass().getSimpleName();
    protected RenameContext context;
    @Override
    public String getName() {
        return name;
    }

    public void setContext(RenameContext context) {
        this.context = context;
    }

    public RenameContext getContext() {
        return context;
    }

    protected Integer[] getPatternIndices(String pattern) {
        return getPatternIndices(pattern, context.mask());
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
        return context.mask().contains(pattern);
    }

    protected String replaceSubstring(@NotNull String string, String replacement, int start, int end) {
        return string.substring(0, start) + replacement + string.substring(end);
    }
}
