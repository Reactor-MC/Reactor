package ink.reactor.nbt.adapter;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class NBTMap implements NBT {
    private final Map<Object, Tag> tags;

    public NBTMap(Map<Object, Tag> tags) {
        this.tags = tags;
    }

    public NBTMap() {
        this.tags = new HashMap<>();
    }

    @Override
    public void add(final Tag tag) {
        tags.put(tag.getKey(), tag);
    }

    @Override
    public void add(final Collection<Tag> tags) {
        for (final Tag tag : tags) {
            this.tags.put(tag.getKey(), tag);
        }
    }

    @Override
    public Tag set(final Tag tag) {
        return tags.put(tag.getKey(), tag);
    }

    @Override
    public Tag remove(final Object key) {
        return tags.remove(key);
    }

    @Override
    public Tag get(final Object key) {
        return tags.get(key);
    }

    @Override
    public Collection<Tag> getTags() {
        return tags.values();
    }

    @Override
    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public void clear() {
        tags.clear();
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        final Collection<Tag> tags = this.tags.values();
        for (final Tag tag : tags) {
            if (tag == null) {
                continue;
            }
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
        return tags.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof NBTMap nbtMap && nbtMap.tags.equals(this.tags));
    }

    public Map<Object, Tag> getMapTags() {
        return tags;
    }
}
