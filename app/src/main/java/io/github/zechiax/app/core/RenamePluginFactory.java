package io.github.zechiax.app.core;

import io.github.zechiax.api.RenamePluginBase;
import org.pf4j.DefaultPluginFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.logging.Logger;

public class RenamePluginFactory extends DefaultPluginFactory {
    private static final Logger LOGGER = Logger.getLogger(RenamePluginFactory.class.getName());

    @Override
    protected Plugin createInstance(Class<?> pluginClass, PluginWrapper pluginWrapper) {
        try {
            return (Plugin) pluginClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.severe("Failed to create instance of plugin: " + pluginClass.getName());
            return null;
        }
    }
}
