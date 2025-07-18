package ink.reactor.nbt.tags;

import java.util.function.Consumer;

public enum TagId {
    END,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BYTE_ARRAY,
    STRING,
    LIST,
    COMPOUND,
    INT_ARRAY,
    LONG_ARRAY;

    private static final TagId[] ALL = values();

    public final byte byteValue;

    TagId() {
        this.byteValue = (byte)ordinal();
    }

    public static TagId byId(final byte id) {
        return (id < 0 || id >= ALL.length) ? null : ALL[id];
    }

    public static void forEach(final Consumer<TagId> consumer) {
        for (final TagId id : ALL) {
            consumer.accept(id);
        }
    }
}
