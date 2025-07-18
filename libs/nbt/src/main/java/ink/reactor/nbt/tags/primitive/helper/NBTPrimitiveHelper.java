package ink.reactor.nbt.tags.primitive.helper;

import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.primitive.*;

public interface NBTPrimitiveHelper {
    void addPrimitiveTag(final NumericalTag numericalTag);
    Tag getPrimitiveTag(final Object key);

    default void addBoolean(final Object key, final boolean value) {
        addPrimitiveTag(new ByteTag(key, value));
    }

    default void addByte(final Object key, final byte value) {
        addPrimitiveTag(new ByteTag(key, value));
    }

    default void addShort(final Object key, final short value) {
        addPrimitiveTag(new ShortTag(key, value));
    }

    default void addInt(final Object key, final int value) {
        addPrimitiveTag(new IntTag(key, value));
    }

    default void addLong(final Object key, final long value) {
        addPrimitiveTag(new LongTag(key, value));
    }

    default void addFloat(final Object key, final float value) {
        addPrimitiveTag(new FloatTag(key, value));
    }

    default void addDouble(final Object key, final double value) {
        addPrimitiveTag(new DoubleTag(key, value));
    }

    default boolean getBoolean(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof ByteTag byteTag && byteTag.getValue() != 0;
    }

    default byte getByte(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.byteValue() : 0;
    }

    default short getShort(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.shortValue() : 0;
    }

    default int getInt(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.intValue() : 0;
    }

    default long getLong(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.longValue() : 0L;
    }

    default float getFloat(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.floatValue() : 0f;
    }

    default double getDouble(Object key) {
        Tag tag = getPrimitiveTag(key);
        return tag instanceof NumericalTag numTag ? numTag.doubleValue() : 0d;
    }
}
