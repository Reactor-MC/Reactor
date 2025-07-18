package ink.reactor.nbt.adapter.readonly;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.adapter.NBTMap;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.Tag;

import java.util.Collection;
import java.util.Map;

public final class NBTReadOnly implements NBT {

    static final UnsupportedOperationException WRITE_EXCEPTION = new UnsupportedOperationException("Read only nbt");
    public final static NBTReadOnly EMPTY_NBT = new NBTReadOnly(Map.of());

    private final NBT nbt;
    private final ReadOnlyCollection readOnlyCollection;

    public NBTReadOnly(Map<Object, Tag> tags) {
        this.nbt = new NBTMap(tags);
        this.readOnlyCollection = new ReadOnlyCollection(nbt, tags.size());
    }

    public NBTReadOnly(NBT nbt) {
        this.nbt = nbt;
        this.readOnlyCollection = new ReadOnlyCollection(nbt, nbt.getTags().size());
    }

    @Override
    public void add(final Tag tag) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public void add(final Collection<Tag> tags) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public Tag set(final Tag tag) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public Tag remove(final Object key) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public Tag get(final Object key) {
        return nbt.get(key);
    }

    @Override
    public Collection<Tag> getTags() {
        return readOnlyCollection;
    }

    @Override
    public boolean isEmpty() {
        return nbt.isEmpty();
    }

    @Override
    public void clear() {
        throw WRITE_EXCEPTION;
    }

    @Override
    public void write(final NBTWriteBuffer buffer) {
        nbt.write(buffer);
    }
}
