package io.github.zechiax.app.plugins;

import io.github.zechiax.app.core.RenamePluginBase;

public abstract class RangePluginBase extends RenamePluginBase {
    protected static boolean isStartEndValid(int start, int end, int length) {
        return start >= 1 && end <= length && start <= end;
    }
}
