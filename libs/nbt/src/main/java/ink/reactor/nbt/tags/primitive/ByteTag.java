package ink.reactor.nbt.tags.primitive;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.TagId;

public final class ByteTag extends NumericalTag {

    private final byte value;

    public ByteTag(final Object key, final byte value) {
        super(key);
        this.value = value;
    }

    public ByteTag(final Object key, final boolean value) {
        super(key);
        this.value = value ? (byte)1 : 0;
    }

    @Override
    public byte byteValue() {
        return value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeByte(value);
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
        return TagId.BYTE;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final ByteTag byteTag)) return false;

        return value == byteTag.value && getKey().equals(byteTag.getKey());
    }

    @Override
    public int hashCode() {
        return value;
    }
}
