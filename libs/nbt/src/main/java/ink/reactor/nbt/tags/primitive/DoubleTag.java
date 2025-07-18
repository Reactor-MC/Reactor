package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class DoubleTag extends NumericalTag {

    private final double value;

    public DoubleTag(final Object key, final double value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeDouble(value);
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
        return (float)value;
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
        return (long)value;
    }

    @Override
    public TagId getId() {
        return TagId.DOUBLE;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 8;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final DoubleTag doubleTag)) return false;

        return Double.compare(value, doubleTag.value) == 0 && getKey().equals(doubleTag.getKey());
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
