package ink.reactor.nbt.tags.object;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class StringByteArrayTag extends Tag {

    private final byte[] value;

    public StringByteArrayTag(Object key, byte[] value) {
        super(key);
        this.value = value;
    }

    public StringByteArrayTag(Object key, String value) {
        super(key);
        this.value = value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeString(value);
    }

    @Override
    public TagId getId() {
        return TagId.STRING;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 2+ value.length;
    }

    @Override
    public String valueToString() {
        return new String(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final StringByteArrayTag that)) return false;

        return Arrays.equals(value, that.value) && getKey().equals(that.getKey());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
