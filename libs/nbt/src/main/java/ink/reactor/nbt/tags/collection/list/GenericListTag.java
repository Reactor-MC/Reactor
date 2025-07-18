package ink.reactor.nbt.tags.collection.list;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;
import ink.reactor.nbt.tags.ListTag;
import lombok.Getter;

import java.util.Collection;
import java.util.Objects;

@Getter
public final class GenericListTag<T extends Tag> extends ListTag {

    private final Collection<T> value;
    private final byte listId;

    public GenericListTag(Object key, Collection<T> values, byte listId) {
        super(key);
        this.listId = listId;
        this.value = values;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeByte(listId);
        buffer.writeInt(value.size());
        for (final T tag : value) {
            tag.write(buffer);
        }
    }

    @Override
    public TagId getId() {
        return TagId.LIST;
    }

    @Override
    public int sizeInBytes() {
        int totalSize = 4;
        for (final T tag : value) {
            totalSize += tag.sizeInBytes();
        }
        return totalSize;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final GenericListTag<?> listTag)) return false;

        return listId == listTag.listId && getKey().equals(listTag.getKey()) && Objects.equals(value, listTag.value);
    }

    @Override
    public int hashCode() {
        return listId;
    }

    @Override
    public Collection<T> getTags() {
        return value;
    }

    @Override
    public byte listId() {
        return listId;
    }
}
