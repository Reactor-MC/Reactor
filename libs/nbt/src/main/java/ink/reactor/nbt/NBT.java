package ink.reactor.nbt;

import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.tags.NumericalTag;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.array.helper.NBTArrayHelper;
import ink.reactor.nbt.tags.collection.helper.NBTCollectionHelper;
import ink.reactor.nbt.tags.object.helper.NBTObjectHelper;
import ink.reactor.nbt.tags.primitive.helper.NBTPrimitiveHelper;

import java.util.Collection;

public interface NBT extends NBTPrimitiveHelper, NBTArrayHelper, NBTObjectHelper, NBTCollectionHelper {
    void add(final Tag tag);
    void add(final Collection<Tag> tags);

    Tag set(final Tag tag);
    Tag remove(final Object key);
    Tag get(final Object key);

    Collection<Tag> getTags();

    boolean isEmpty();
    void clear();

    void write(final NBTWriteBuffer buffer);

    @Override
    default void addPrimitiveTag(final NumericalTag numericalTag) {
        add(numericalTag);
    }
    @Override
    default void addArrayTag(final Tag arrayTag) {
        add(arrayTag);
    }
    @Override
    default void addObjectTag(final Tag objectTag) {
        add(objectTag);
    }
    @Override
    default void addCollectionTag(final Tag collectionTag) {
        add(collectionTag);
    }

    @Override
    default Tag getCollectionTag(Object key) {
        return get(key);
    }
    @Override
    default Tag getObjectTag(Object key) {
        return get(key);
    }
    @Override
    default Tag getPrimitiveTag(final Object key) {
        return get(key);
    }
    @Override
    default Tag getArrayTag(Object key) {
        return get(key);
    }
}
