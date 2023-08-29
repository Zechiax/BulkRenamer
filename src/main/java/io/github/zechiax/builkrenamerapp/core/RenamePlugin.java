package io.github.zechiax.builkrenamerapp.core;

import java.util.ArrayList;

public interface RenamePlugin {
    public String getName();
    public ArrayList<String> rename(RenameContext context);
}
