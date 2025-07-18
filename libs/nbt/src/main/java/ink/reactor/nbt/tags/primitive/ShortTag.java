package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class ShortTag extends NumericalTag {

    private final short value;

    public ShortTag(final Object key, final short value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeShort(value);
    }

    @Override
    public byte byteValue() {
        return (byte)value;
    }

    @Override
    public short shortValue() {
        return value;
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
        return TagId.SHORT;
    }

    @Override
    public Short getValue() {
        return value;
    }
    @Override
    public int sizeInBytes() {
        return 2;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final ShortTag shortTag)) return false;

        return value == shortTag.value && getKey().equals(shortTag.getKey());
    }

    @Override
    public int hashCode() {
        return value;
    }
}
