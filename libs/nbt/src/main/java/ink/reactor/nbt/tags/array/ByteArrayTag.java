package ink.reactor.nbt.tags.array;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;
import lombok.Getter;

import java.util.Arrays;

@Getter
public final class ByteArrayTag extends Tag {

    private final byte[] value;

    public ByteArrayTag(Object key, byte[] value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeBytes(value);
    }

    @Override
    public TagId getId() {
        return TagId.BYTE_ARRAY;
    }

    @Override
    public int sizeInBytes() {
        return 4 + value.length;
    }

    @Override
    public String valueToString() {
        return Arrays.toString(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final ByteArrayTag that)) return false;

        return Arrays.equals(value, that.value) && getKey().equals(that.getKey());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
