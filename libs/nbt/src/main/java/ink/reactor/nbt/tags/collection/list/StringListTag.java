package ink.reactor.nbt.tags.collection.list;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.ListTag;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;
import ink.reactor.nbt.tags.object.StringTag;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;

@Getter
public final class StringListTag extends ListTag {
    private final Collection<String> value;

    public StringListTag(Object key, final Collection<String> value) {
        super(key);
        this.value = value;
    }

    @Override
    public TagId getId() {
        return TagId.LIST;
    }

    @Override
    public int sizeInBytes() {
        int totalSize = 4;
        for (final String tag : value) {
            totalSize += tag.length();
        }
        return totalSize;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        buffer.writeByte(listId());
        buffer.writeInt(value.size());
        for (final String string : value) {
            buffer.writeString(string);
        }
    }

    @Override
    public Collection<? extends Tag> getTags() {
        final StringTag[] stringTags = new StringTag[value.size()];
        int i = 0;
        for (final String string : value) {
            stringTags[i] = new StringTag(null, string);
        }
        return Arrays.asList(stringTags);
    }

    @Override
    public int hashCode() {
        return TagId.STRING.byteValue;
    }

    @Override
    public byte listId() {
        return TagId.STRING.byteValue;
    }
}
