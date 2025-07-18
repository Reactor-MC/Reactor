package ink.reactor.nbt.adapter;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
public final class NBTArray implements NBT {

    private Tag[] tags;
    private int index;

    public NBTArray(Tag[] tags) {
        this.tags = tags;
    }

    public NBTArray() {
        this.tags = new Tag[16];
    }

    @Override
    public void add(final Tag tag) {
        if (index >= tags.length) {
            Tag[] newTags = new Tag[tags.length * 2];
            System.arraycopy(tags, 0, newTags, 0, tags.length);
            this.tags = newTags;
        }

        tags[index++] = tag;
    }

    @Override
    public void add(final Collection<Tag> tags) {
        for (Tag tag : tags) {
            add(tag);
        }
    }

    @Override
    public Tag set(final Tag tag) {
        for (int i = 0; i < index; i++) {
            if (tags[i].getKey().equals(tag.getKey())) {
                Tag oldTag = tags[i];
                tags[i] = tag;
                return oldTag;
            }
        }
        add(tag);
        return null;
    }

    @Override
    public Tag remove(final Object key) {
        Tag removedTag = null;
        int removeIndex = -1;

        for (int i = 0; i < index; i++) {
            if (tags[i].getKey().equals(key)) {
                removedTag = tags[i];
                removeIndex = i;
                break;
            }
        }

        if (removeIndex != -1) {
            System.arraycopy(tags, removeIndex + 1, tags, removeIndex, index - removeIndex - 1);
            tags[--index] = null;
        }

        return removedTag;
    }

    @Override
    public Tag get(final Object key) {
        for (Tag tag : tags) {
            if (tag.getKey().equals(key)) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public Collection<Tag> getTags() {
        return Arrays.asList(tags).subList(0, index);
    }

    @Override
    public boolean isEmpty() {
        return index == 0;
    }

    @Override
    public void clear() {
        this.tags = new Tag[16];
        this.index = 0;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        for (int i = 0; i < index; i++) {
            final Tag tag = tags[i];

            buffer.writeByte(tag.getId().byteValue);
            final Object key = tag.getKey();

            if (key instanceof byte[] bytes) {
                buffer.writeString(bytes);
            } else {
                buffer.writeString(key.toString());
            }

            tag.write(buffer);
        }
    }

    @Override
    public String toString() {
        return Arrays.asList(tags).subList(0, index).toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof NBTArray nbtArray && Arrays.equals(nbtArray.tags, this.tags));
    }
}
