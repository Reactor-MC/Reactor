package ink.reactor.kernel.plugin;

import lombok.Getter;

@Getter
public abstract class Plugin {
    private PluginData data;

    protected void onLoad(){}
    protected void onEnable(){}
    protected void onDisable(){}

    protected void setData(final PluginData data) {
        if (this.data != null && this.data.privacy() == PluginPrivacy.PUBLIC) {
            throw new IllegalStateException("Plugin data is already set");
        }
        this.data = data;
    }
}