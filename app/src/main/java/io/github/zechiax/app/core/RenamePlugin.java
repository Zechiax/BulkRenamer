package io.github.zechiax.app.core;

public interface RenamePlugin {
    public String getName();
    public String rename(RenameContext context) throws RenamingException;
}
