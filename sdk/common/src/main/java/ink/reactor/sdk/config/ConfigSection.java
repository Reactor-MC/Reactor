package ink.reactor.sdk.config;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ConfigSection {
    String getName();

    Object get(final String key);
    <T> T get(final String key, final Class<T> clazz);
    <T> T getOrDefault(final String key, final T defaultValue);
    <T> List<T> getList(final String key, final Class<T> clazz);

    Collection<String> getKeys();
    Collection<?> getValues();

    Map<String, ?> getData();

    String getString(final String key);

    double getDouble(final String key);
    long getLong(final String key);
    int getInt(final String key);
    float getFloat(final String key);
    char getChar(final String key);
    boolean getBoolean(final String key);

    ConfigSection getSection(final String key);

    default List<String> getStringList(final String key) {
        return getList(key, String.class);
    }

    void forEachEntry(final BiConsumer<String, Object> consumer);

    Object set(final String key, final Object object);

    // Example: set("key1", "value", "key2", 25, "key3", "value3");
    default void set(final Object... keyAndValues) {
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

    boolean isEmpty();
}
