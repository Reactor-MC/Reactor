package ink.reactor.sdk.config.section;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MapConfigSection extends AbstractConfigSection {

    private final Map<String, Object> data;

    public MapConfigSection(Map<String, Object> data, String name) {
        super(name);
        this.data = data;
    }

    public MapConfigSection(Map<String, Object> data) {
        super("");
        this.data = data;
    }

    public MapConfigSection(String name) {
        super(name);
        this.data = new LinkedHashMap<>();
    }

    public MapConfigSection() {
        super("");
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Object get(final String key) {
        return data.get(key);
    }

    @Override
    public @NotNull Map<String, ?> getData() {
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConfigSection getSection(final String key) {
        final Object object = get(key);
        if (object instanceof ConfigSection configSection) {
            return configSection;
        }
        return (object instanceof Map) ? new MapConfigSection(
            (Map<String, Object>) object,
            (name.isEmpty()) ? key : name+'.'+key
        ) : null;
    }

    @Override
    public ConfigSection getOrCreateSection(final String key) {
        final ConfigSection section = getSection(key);
        if (section != null) {
            return section;
        }
        final MapConfigSection value = new MapConfigSection((name.isEmpty()) ? key : name + '.' + key);
        data.put(key, value);
        return value;
    }

    @Override
    public Object set(final String key, final Object object) {
        return data.put(key, object);
    }
}
