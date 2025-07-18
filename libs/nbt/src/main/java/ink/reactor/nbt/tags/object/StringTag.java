package ink.reactor.nbt.tags.object;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;

import java.util.Objects;

public final class StringTag extends Tag {

    private final String value;

    public StringTag(Object key, String value) {
        super(key);
        this.value = value;
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
    public String getValue() {
        return value;
    }

    @Override
    public int sizeInBytes() {
        return 2 + value.length();
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final StringTag stringTag)) return false;

        return Objects.equals(value, stringTag.value) && getKey().equals(stringTag.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
