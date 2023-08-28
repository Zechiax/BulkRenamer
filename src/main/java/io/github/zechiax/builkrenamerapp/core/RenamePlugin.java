package io.github.zechiax.builkrenamerapp.core;

public interface RenamePlugin {
    public String getName();
    public String[] rename(RenameContext context);
}
