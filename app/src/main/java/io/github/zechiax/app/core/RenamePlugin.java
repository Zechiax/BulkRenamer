package io.github.zechiax.app.core;

public interface RenamePlugin {
    public String getName();
    public String rename(PluginContext context) throws RenamingException;
}
