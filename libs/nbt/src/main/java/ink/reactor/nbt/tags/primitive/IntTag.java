package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class IntTag extends NumericalTag {

    private final int value;

    public IntTag(final Object key, final int value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeInt(value);
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
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public TagId getId() {
        return TagId.INT;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 4;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final IntTag intTag)) return false;

        return value == intTag.value && getKey().equals(intTag.getKey());
    }

    @Override
    public int hashCode() {
        return value;
    }
}
