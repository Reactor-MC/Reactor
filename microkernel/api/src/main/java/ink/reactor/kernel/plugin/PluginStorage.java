package ink.reactor.kernel.plugin;

import java.util.Collection;

public interface PluginStorage {
    /**
     *  Get plugin by name
     *  @return null or the plugin if his privacy is public or strict-mode
     */
    Plugin getPlugin(final String pluginName);

    /**
     *  Get all plugins
     *  @return a collection of plugins with privacy in public or strict-mode
     */
    Collection<Plugin> getPlugins();
}