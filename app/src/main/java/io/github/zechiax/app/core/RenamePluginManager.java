package io.github.zechiax.app.core;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginFactory;

public class RenamePluginManager extends DefaultPluginManager {

    @Override
    protected PluginFactory createPluginFactory() {
        return new RenamePluginFactory();
    }
}
