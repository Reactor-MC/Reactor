package ink.reactor.nbt.tags.array.helper;

import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.array.ByteArrayTag;
import ink.reactor.nbt.tags.array.IntArrayTag;
import ink.reactor.nbt.tags.array.LongArrayTag;
import ink.reactor.nbt.tags.object.UUIDTag;

import java.util.UUID;

public interface NBTArrayHelper {
    void addArrayTag(final Tag arrayTag);
    Tag getArrayTag(Object key);

    default byte[] getByteArray(Object key) {
        Tag tag = getArrayTag(key);
        return tag instanceof ByteArrayTag byteArrayTag ? byteArrayTag.getValue() : null;
    }

    default int[] getIntArray(Object key) {
        Tag tag = getArrayTag(key);
        return tag instanceof IntArrayTag intArrayTag ? intArrayTag.getValue() : null;
    }

    default long[] getLongArray(Object key) {
        Tag tag = getArrayTag(key);
        return tag instanceof LongArrayTag longArrayTag ? longArrayTag.getValue() : null;
    }

    default int[] getUUIDAsIntArray(Object key) {
        Tag tag = getArrayTag(key);
        if (tag instanceof UUIDTag uuidTag) {
            UUID uuid = uuidTag.getValue();
            return new int[] {
                (int)(uuid.getMostSignificantBits() >> 32),
                (int)uuid.getMostSignificantBits(),
                (int)(uuid.getLeastSignificantBits() >> 32),
                (int)uuid.getLeastSignificantBits()
            };
        }
        return null;
    }

    default void addIntArray(final Object key, final int... intArray) {
        if (intArray.length != 0) {
            addArrayTag(new IntArrayTag(key, intArray));
        }
    }

    default void addByteArray(final Object key, final byte... bytes) {
        if (bytes.length != 0) {
            addArrayTag(new ByteArrayTag(key, bytes));
        }
    }

    default void addLongArray(final Object key, final long... longs) {
        if (longs.length != 0) {
            addArrayTag(new LongArrayTag(key, longs));
        }
    }
}
