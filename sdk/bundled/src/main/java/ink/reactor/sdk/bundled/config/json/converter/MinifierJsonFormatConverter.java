package ink.reactor.sdk.bundled.config.json.converter;

import ink.reactor.sdk.config.section.ConfigSection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class MinifierJsonFormatConverter {

    private final StringBuilder builder = new StringBuilder();

    public String toJson(final ConfigSection configSection) {
        appendSection(configSection);
        return builder.toString();
    }

    private void appendObject(final Object value) {
        switch (value) {
            case null -> builder.append("null");
            case Boolean bool -> builder.append(bool.booleanValue());
            case ConfigSection subSection -> appendSection(subSection);
            case Number _ -> builder.append(value);
            case Collection<?> collection -> appendCollection(collection);
            default -> builder.append('"').append(value).append('"');
        }
    }

    private void appendSection(final ConfigSection section) {
        if (section.isEmpty()) {
            builder.append("{}");
            return;
        }

        builder.append('{');
        final Set<? extends Map.Entry<String, ?>> entries = section.getData().entrySet();
        final int size = entries.size();
        int i = 0;

        for (final Map.Entry<String, ?> entry : entries) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            builder.append('"').append(key).append('"').append(':');
            appendObject(value);
            if (++i != size) {
                builder.append(',');
            }
        }
        builder.append('}');
    }

    private void appendCollection(final Collection<?> collection) {
        builder.append('[');
        final int size = collection.size();
        int i = 0;
        for (final Object o : collection) {
            appendObject(o);
            if (++i != size) {
                builder.append(',');
            }
        }
        builder.append(']');
    }
}
