package ink.reactor.kernel.plugin;

import java.util.List;

public record PluginData(
    String version,

    List<String> authors,
    List<String> dependencies,
    List<String> softDependencies,

    PluginPrivacy privacy
) {}