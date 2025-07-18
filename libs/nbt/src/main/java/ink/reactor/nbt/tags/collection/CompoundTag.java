package ink.reactor.nbt.tags.collection;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class CompoundTag extends Tag {

    private final NBT value;

    public CompoundTag(Object key, NBT value) {
        super(key);
        this.value = value;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        value.write(buffer);
        buffer.writeByte(TagId.END.byteValue);
    }

    @Override
    public TagId getId() {
        return TagId.COMPOUND;
    }

    @Override
    public int sizeInBytes() {
        return -1;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final CompoundTag that)) return false;

        return getKey().equals(that.getKey()) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
