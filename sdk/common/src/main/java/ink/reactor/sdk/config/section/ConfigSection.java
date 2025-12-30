package ink.reactor.sdk.config.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ConfigSection {
    @NotNull String getName();

    @Nullable Object get(final String key);
    @Nullable <T> T get(final String key, final Class<T> clazz);
    <T> T getOrDefault(final String key, final T defaultValue);
    <T> T getOrCreate(final String key, final T defaultValue);
    @NotNull <T> List<T> getList(final String key, final Class<T> clazz);

    @NotNull Collection<String> getKeys();
    @NotNull Collection<?> getValues();

    @NotNull Map<String, ?> getData();

    @Nullable String getString(final String key);

    double getDouble(final String key);
    long getLong(final String key);
    int getInt(final String key);
    float getFloat(final String key);
    char getChar(final String key);
    boolean getBoolean(final String key);

    double getDouble(final String key, final double defaultReturn);
    long getLong(final String key, final long defaultReturn);
    int getInt(final String key, final int defaultReturn);
    float getFloat(final String key, final float defaultReturn);
    char getChar(final String key, final char defaultReturn);
    boolean getBoolean(final String key, final boolean defaultReturn);

    @Nullable ConfigSection getSection(final String key);
    ConfigSection getOrCreateSection(final String key);

    default @NotNull List<String> getStringList(final String key) {
        return getList(key, String.class);
    }

    void forEachEntry(final BiConsumer<String, Object> consumer);

    @Nullable Object set(final String key, final Object object);

    // Example: set("key1", "value", "key2", 25, "key3", "value3");
    void set(final Object... keyAndValues);

    boolean isEmpty();
}
