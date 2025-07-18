package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class FloatTag extends NumericalTag {

    private final float value;

    public FloatTag(final Object key, final float value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeFloat(value);
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
        return (long)value;
    }

    @Override
    public TagId getId() {
        return TagId.FLOAT;
    }

    @Override
    public Float getValue() {
        return value;
    }
    @Override
    public int sizeInBytes() {
        return 4;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final FloatTag floatTag)) return false;

        return Float.compare(value, floatTag.value) == 0 && getKey().equals(floatTag.getKey());
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }
}
