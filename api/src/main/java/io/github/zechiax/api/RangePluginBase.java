package io.github.zechiax.api;

import io.github.zechiax.api.RenamePluginBase;

public abstract class RangePluginBase extends RenamePluginBase {
    protected static boolean isStartEndValid(int start, int end, int length) {
        return start >= 1 && end <= length && start <= end;
    }
}
