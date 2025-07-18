package ink.reactor.nbt.adapter.readonly;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

import static ink.reactor.nbt.adapter.readonly.NBTReadOnly.WRITE_EXCEPTION;

@RequiredArgsConstructor
final class ReadOnlyCollection implements Collection<Tag> {
    private final NBT nbt;
    private final int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(final Object o) {
        return nbt.get(o) != null;
    }

    @Override
    public Iterator<Tag> iterator() {
        final Iterator<Tag> iterator = nbt.getTags().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Tag next() {
                return iterator.next();
            }
            @Override
            public void remove() {
                throw WRITE_EXCEPTION;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return nbt.getTags().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return nbt.getTags().toArray(a);
    }

    @Override
    public boolean add(final Tag tag) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public boolean remove(final Object o) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends Tag> c) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw WRITE_EXCEPTION;
    }

    @Override
    public void clear() {
        throw WRITE_EXCEPTION;
    }
}
