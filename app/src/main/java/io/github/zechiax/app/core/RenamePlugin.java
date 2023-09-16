package io.github.zechiax.app.core;

import io.github.zechiax.api.PluginContext;

public interface RenamePlugin {
    public String getName();
    public String rename(PluginContext context) throws RenamingException;
}
