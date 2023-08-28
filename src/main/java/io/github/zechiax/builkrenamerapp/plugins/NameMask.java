package io.github.zechiax.builkrenamerapp.plugins;

import io.github.zechiax.builkrenamerapp.core.RenameContext;
import io.github.zechiax.builkrenamerapp.core.RenamePlugin;

public class NameMask implements RenamePlugin {
    @Override
    public String getName() {
        return "Name Mask";
    }

    @Override
    public String[] rename(RenameContext context) {
        return new String[0];
    }
}
