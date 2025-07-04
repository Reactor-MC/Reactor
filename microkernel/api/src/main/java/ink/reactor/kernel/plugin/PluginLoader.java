package ink.reactor.kernel.plugin;

import java.util.jar.JarFile;

public interface PluginLoader {
    void load(final JarFile pluginFile);
    void load(final Plugin plugin, PluginData pluginData);
}
