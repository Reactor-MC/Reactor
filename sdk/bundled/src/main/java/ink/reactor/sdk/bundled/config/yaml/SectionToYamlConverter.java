package ink.reactor.sdk.bundled.config.yaml;

import ink.reactor.sdk.config.ConfigSection;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public final class SectionToYamlConverter {

    private final int indentSpaces;
    private final StringBuilder builder = new StringBuilder();
    private int indentLevel = -1;

    public String toYaml(final ConfigSection configSection) {
        appendSection(configSection);
        return builder.toString();
    }

    private void appendSection(final ConfigSection subSection) {
        indentLevel++;

        final Set<? extends Map.Entry<String, ?>> entries = subSection.getData().entrySet();
        final int size = entries.size();
        int i = 0;

        for (final Map.Entry<String, ?> entry : entries) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            builder.repeat(' ', indentLevel * indentSpaces);
            builder.append(key).append(':').append(' ');
            appendObject(value);
            if (++i != size) {
                builder.append('\n');
            }
        }

        indentLevel--;
    }

    private void appendObject(final Object value) {
        switch (value) {
            case null -> builder.append("null");
            case Boolean bool -> builder.append(bool.booleanValue());
            case ConfigSection subSection -> {
                builder.append('\n');
                appendSection(subSection);
            }
            case Number _ -> builder.append(value);
            case Collection<?> collection -> appendCollection(collection);
            default -> builder.append('"').append(value).append('"');
        }
    }

    private void appendCollection(final Collection<?> collection) {
        if (collection.isEmpty()) {
            builder.append("[]");
            return;
        }

        builder.append('\n');
        final int size = collection.size();
        int i = 0;
        for (final Object o : collection) {
            builder.repeat(' ', indentSpaces * (indentLevel+1)).append('-').append(' ');
            appendObject(o);
            if (++i != size) {
                builder.append('\n');
            }
        }
    }
}
