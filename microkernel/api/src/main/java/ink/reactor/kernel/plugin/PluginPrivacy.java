package ink.reactor.kernel.plugin;

public enum PluginPrivacy {
    /**
     * The plugin can access the api. But no one else can access this
     */
    PRIVATE,

    /**
     * You need to specify exactly which classes or packages you want to expose to others
     */
    STRICT_MODE,

    /**
     * Anyone can access both the API and the plugin's internal code
     */
    PUBLIC,
}