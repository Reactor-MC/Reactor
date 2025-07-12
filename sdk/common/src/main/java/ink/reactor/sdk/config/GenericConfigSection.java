package ink.reactor.sdk.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
@Getter
public class GenericConfigSection implements ConfigSection {

    private final String name;
    private final Map<String, Object> data;

    public GenericConfigSection(Map<String, Object> data) {
        this.name = "";
        this.data = data;
    }

    public GenericConfigSection() {
        this.name = "";
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Object get(final String key) {
        return data.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final Class<T> clazz) {
        final Object object = data.get(key);
        return (object != null && object.getClass().equals(clazz)) ? (T) object : null;
    }

    @Override
    public <T> T getOrDefault(final String key, final T defaultValue) {
        @SuppressWarnings("unchecked")
        final T value = get(key, (Class<T>) defaultValue.getClass());
        return (value == null) ? defaultValue : value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(final String key, final Class<T> clazz) {
        if (!(data.get(key) instanceof List<?> list)) {
            return null;
        }
        list.removeIf(next -> next == null || !(next.getClass().equals(clazz)));
        return (List<T>) list;
    }

    @Override
    public char getChar(final String key) {
        final Object o = get(key);
        return switch (o) {
            case Character character -> character;
            case String s -> s.charAt(0);
            default -> 0;
        };
    }

    @Override
    public List<String> getStringList(final String key) {
        return getList(key, String.class);
    }

    @Override
    public Collection<String> getKeys() {
        return data.keySet();
    }

    @Override
    public Collection<?> getValues() {
        return data.values();
    }

    @Override
    public void forEachEntry(final BiConsumer<String, Object> consumer) {
        final Collection<Map.Entry<String, Object>> entrySet = data.entrySet();
        for (final Map.Entry<String, Object> entry : entrySet) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
    @Override
    public String getString(final String key) {
        final Object object = data.get(key);
        return (object != null) ? object.toString() : null;
    }

    @Override
    public int getInt(final String key) {
        return (data.get(key) instanceof Number number) ? number.intValue() : 0;
    }

    @Override
    public boolean getBoolean(final String key) {
        return (data.get(key) instanceof Boolean b) ? b : false;
    }

    @Override
    public long getLong(final String key) {
        return (data.get(key) instanceof Number number) ? number.longValue() : 0;
    }

    @Override
    public double getDouble(final String key) {
        return (data.get(key) instanceof Number number) ? number.doubleValue() : 0;
    }

    @Override
    public float getFloat(final String key) {
        return (data.get(key) instanceof Number number) ? number.floatValue() : 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConfigSection getSection(final String key) {
        final Object object = get(key);
        if (object instanceof ConfigSection configSection) {
            return configSection;
        }
        return (object instanceof Map) ? new GenericConfigSection(name+'.'+key, (Map<String, Object>) object) : null;
    }

    @Override
    public Object set(final String key, final Object object) {
        return data.put(key, object);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof ConfigSection configSection && configSection.getData().equals(this.data);
    }
}
