package ink.reactor.nbt.tags.object;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;

import java.util.Objects;
import java.util.UUID;

public final class UUIDTag extends Tag {

    private final UUID value;

    public UUIDTag(Object key, UUID value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        final long mostSig = value.getMostSignificantBits();
        final long leastSig = value.getLeastSignificantBits();

        buffer.writeInt(4); // UUID in minecraft is an int array. UUID contains 128 bits = 4 int
        buffer.writeLong(mostSig);
        buffer.writeLong(leastSig);
    }

    @Override
    public TagId getId() {
        return TagId.INT_ARRAY;
    }

    @Override
    public UUID getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 16;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final UUIDTag uuidTag)) return false;

        return Objects.equals(value, uuidTag.value) && getKey().equals(uuidTag.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
