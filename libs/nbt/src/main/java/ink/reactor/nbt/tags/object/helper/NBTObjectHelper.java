package ink.reactor.nbt.tags.object.helper;

import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.object.StringByteArrayTag;
import ink.reactor.nbt.tags.object.StringTag;
import ink.reactor.nbt.tags.object.UUIDTag;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public interface NBTObjectHelper {
    void addObjectTag(final Tag objectTag);
    Tag getObjectTag(Object key);

    default String getString(Object key) {
        Tag tag = getObjectTag(key);
        if (tag instanceof StringTag stringTag) {
            return stringTag.getValue();
        } else if (tag instanceof StringByteArrayTag byteArrayTag) {
            return byteArrayTag.valueToString();
        }
        return null;
    }

    default byte[] getStringAsBytes(Object key) {
        Tag tag = getObjectTag(key);
        if (tag instanceof StringByteArrayTag byteArrayTag) {
            return byteArrayTag.getValue();
        } else if (tag instanceof StringTag stringTag) {
            return stringTag.getValue().getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }

    default UUID getUUID(Object key) {
        Tag tag = getObjectTag(key);
        return tag instanceof UUIDTag uuidTag ? uuidTag.getValue() : null;
    }

    default void addString(final Object key, final String string) {
        addObjectTag(new StringTag(key, string));
    }

    default void addString(final Object key, final byte[] cachedString) {
        addObjectTag(new StringByteArrayTag(key, cachedString));
    }

    default void addUUID(final Object key, final UUID uuid) {
        addObjectTag(new UUIDTag(key, uuid));
    }
}
