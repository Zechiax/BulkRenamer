package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenamePluginBase;

public abstract class RangePluginBase extends RenamePluginBase {
    protected static boolean isStartEndValid(int start, int end, int length) {
        return start >= 0 && end <= length && start <= end;
    }
}
