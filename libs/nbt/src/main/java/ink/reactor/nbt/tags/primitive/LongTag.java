package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class LongTag extends NumericalTag {

    private final long value;

    public LongTag(final Object key, final long value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeLong(value);
    }

    @Override
    public byte byteValue() {
        return (byte)value;
    }

    @Override
    public short shortValue() {
        return (short)value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int intValue() {
        return (int)value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public TagId getId() {
        return TagId.LONG;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 8;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final LongTag longTag)) return false;

        return value == longTag.value && getKey().equals(longTag.getKey());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }
}
