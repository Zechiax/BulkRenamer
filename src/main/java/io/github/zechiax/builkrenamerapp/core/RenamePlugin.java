package io.github.zechiax.builkrenamerapp.core;

import java.util.ArrayList;

public interface RenamePlugin {
    public String getName();
    public String rename(RenameContext context) throws RenamingException;
}
