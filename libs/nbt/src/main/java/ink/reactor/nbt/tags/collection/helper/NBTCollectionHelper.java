package ink.reactor.nbt.tags.collection.helper;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.TagId;
import ink.reactor.nbt.tags.collection.CompoundTag;
import ink.reactor.nbt.tags.collection.list.GenericListTag;
import ink.reactor.nbt.tags.collection.list.StringListTag;
import ink.reactor.nbt.tags.object.StringTag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface NBTCollectionHelper {
    void addCollectionTag(final Tag collectionTag);
    Tag getCollectionTag(Object key);

    default List<String> getStringList(Object key) {
        Tag tag = getCollectionTag(key);
        if (tag instanceof GenericListTag<?> listTag && listTag.listId() == TagId.STRING.byteValue) {
            return listTag.getValue().stream()
                .map(t -> (StringTag)t)
                .map(StringTag::getValue)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    default <T extends Tag> List<T> getList(Object key, Class<T> tagType) {
        Tag tag = getCollectionTag(key);
        if (tag instanceof GenericListTag<?> listTag) {
            return listTag.getValue().stream()
                .filter(tagType::isInstance)
                .map(tagType::cast)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    default CompoundTag getCompound(Object key) {
        Tag tag = getCollectionTag(key);
        return tag instanceof CompoundTag compoundTag ? compoundTag : null;
    }

    default void addSection(final Object key, final NBT subSection) {
        addCollectionTag(new CompoundTag(key, subSection));
    }

    default void addStrings(final Object key, final String... strings) {
        if (strings.length != 0) {
            addCollectionTag(new StringListTag(key, Arrays.asList(strings)));
        }
    }

    default void addStringCollection(final Object key, final Collection<? extends Tag> tags) {
        if (!tags.isEmpty()) {
            addCollectionTag(new GenericListTag<>(key, tags, TagId.STRING.byteValue));
        }
    }

    default void addCollection(final Object key, final Collection<? extends Tag> tags, final byte id) {
        if (!tags.isEmpty()) {
            addCollectionTag(new GenericListTag<>(key, tags, id));
        }
    }
}
