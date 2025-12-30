package ink.reactor.sdk.config.section;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public abstract class AbstractConfigSection implements ConfigSection {

    @Getter
    protected final String name;

    @Override
    public @NotNull Collection<String> getKeys() {
        return getData().keySet();
    }

    @Override
    public @NotNull Collection<?> getValues() {
        return getData().values();
    }

    @Override
    public void forEachEntry(final BiConsumer<String, Object> consumer) {
        final Set<? extends Map.Entry<String, ?>> entrySet = getData().entrySet();
        for (final Map.Entry<String, ?> entry : entrySet) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public <T> T getOrDefault(final String key, final T defaultValue) {
        @SuppressWarnings("unchecked")
        final T value = get(key, (Class<T>) defaultValue.getClass());
        return (value == null) ? defaultValue : value;
    }

    @Override
    public <T> T getOrCreate(final String key, final T defaultValue) {
        @SuppressWarnings("unchecked")
        final T value = get(key, (Class<T>) defaultValue.getClass());
        if (value != null) {
            return value;
        }
        set(key, defaultValue);
        return defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final Class<T> clazz) {
        final Object object = get(key);
        return (object != null && object.getClass().equals(clazz)) ? (T) object : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull List<T> getList(final String key, final Class<T> clazz) {
        if (!(get(key) instanceof List<?> list)) {
            return List.of();
        }
        list.removeIf(next -> next == null || !(next.getClass().equals(clazz)));
        return (List<T>) list;
    }

    @Override
    public @NotNull List<String> getStringList(final String key) {
        return getList(key, String.class);
    }

    @Override
    public String getString(final String key) {
        final Object object = get(key);
        return (object != null) ? object.toString() : null;
    }

    @Override
    public int getInt(final String key) {
        return getInt(key, 0);
    }

    @Override
    public boolean getBoolean(final String key) {
        return getBoolean(key, false);
    }

    @Override
    public long getLong(final String key) {
        return getLong(key, 0);
    }

    @Override
    public double getDouble(final String key) {
        return getDouble(key, 0);
    }

    @Override
    public float getFloat(final String key) {
        return getFloat(key, 0);
    }

    @Override
    public double getDouble(final String key, final double defaultReturn) {
        return (get(key) instanceof Number number) ? number.doubleValue() : defaultReturn;
    }

    @Override
    public long getLong(final String key, final long defaultReturn) {
        return (get(key) instanceof Number number) ? number.longValue() : defaultReturn;
    }

    @Override
    public int getInt(final String key, final int defaultReturn) {
        return (get(key) instanceof Number number) ? number.intValue() : defaultReturn;
    }

    @Override
    public float getFloat(final String key, final float defaultReturn) {
        return (get(key) instanceof Number number) ? number.floatValue() : defaultReturn;
    }

    @Override
    public char getChar(final String key, final char defaultReturn) {
        final Object o = get(key);
        return o == null ? defaultReturn : switch (o) {
            case Character character -> character;
            case String s -> s.charAt(0);
            default -> {
                final String string = o.toString();
                yield string.isEmpty() ? defaultReturn : string.charAt(0);
            }
        };
    }

    @Override
    public boolean getBoolean(final String key, final boolean defaultReturn) {
        return (get(key) instanceof Boolean b) ? b : defaultReturn;
    }

    @Override
    public char getChar(final String key) {
        return getChar(key, (char)0);
    }

    @Override
    public void set(final Object... keyAndValues) {
        if (keyAndValues.length % 2 != 0) {
            throw new RuntimeException("Key and values format: key, value, key, value.");
        }
        String keyString = null;
        for (Object object : keyAndValues) {
            if (keyString == null) {
                keyString = object.toString();
                continue;
            }
            set(keyString, object);
            keyString = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return getData().isEmpty();
    }

    @Override
    public String toString() {
        return getData().toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof ConfigSection configSection && configSection.getData().equals(this.getData());
    }
}
