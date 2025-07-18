package ink.reactor.nbt.buffer;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.adapter.NBTMap;
import ink.reactor.nbt.adapter.readonly.NBTReadOnly;
import ink.reactor.nbt.tags.Tag;
import ink.reactor.nbt.tags.array.ByteArrayTag;
import ink.reactor.nbt.tags.array.IntArrayTag;
import ink.reactor.nbt.tags.array.LongArrayTag;
import ink.reactor.nbt.tags.collection.CompoundTag;
import ink.reactor.nbt.tags.ListTag;
import ink.reactor.nbt.tags.collection.list.GenericListTag;
import ink.reactor.nbt.tags.object.StringTag;
import ink.reactor.nbt.tags.primitive.*;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public final class NBTReadBuffer {

    private final byte[] buffer;
    private int index;

    public NBTReadBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public NBTReadBuffer(byte[] buffer, int index) {
        this.buffer = buffer;
        this.index = index;
    }

    public byte readByte() {
        return buffer[index++];
    }

    public int readUnsignedByte() {
        return buffer[index++] & 0xFF;
    }

    public short readShort() {
        return (short)((buffer[index++] & 0xFF) << 8 | (buffer[index++] & 0xFF));
    }

    public int readInt() {
        return (buffer[index++] & 0xFF) << 24 |
            (buffer[index++] & 0xFF) << 16 |
            (buffer[index++] & 0xFF) << 8 |
            (buffer[index++] & 0xFF);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public long readLong() {
        return ((long)(buffer[index++] & 0xFF) << 56) |
            ((long)(buffer[index++] & 0xFF) << 48) |
            ((long)(buffer[index++] & 0xFF) << 40) |
            ((long)(buffer[index++] & 0xFF) << 32) |
            ((long)(buffer[index++] & 0xFF) << 24) |
            ((long)(buffer[index++] & 0xFF) << 16) |
            ((long)(buffer[index++] & 0xFF) << 8) |
            ((long)(buffer[index++] & 0xFF));
    }

    public byte[] readBytes() {
        final int length = readInt();
        final byte[] result = new byte[length];
        System.arraycopy(buffer, index, result, 0, length);
        index += length;
        return result;
    }

    public int[] readIntArray() {
        final int length = readInt();
        final int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt();
        }
        return array;
    }

    public long[] readLongArray() {
        final int length = readInt();
        long[] array = new long[length];
        for (int index = 0; index < length; index++) {
            array[index] = readLong();
        }
        return array;
    }

    public String readString() {
        final int length = readShort();
        final byte[] bytes = new byte[length];
        System.arraycopy(buffer, index, bytes, 0, length);
        index += length;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public UUID readUUID() {
        readInt(); // Skip 4 bytes of length
        return new UUID(readLong(), readLong());
    }

    public ListTag readList(final String key) {
        final byte id = readByte();
        final int size = readInt();
        final ArrayList<Tag> tags = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            final Tag tag = readTag(id, null);
            if (tag != null) {
                tags.add(tag);
            }
        }

        return new GenericListTag<>(key, tags, id);
    }

    public Tag readTag(final byte tagId, final String key) {
        return switch (tagId) {
            case 0 -> new CompoundTag(key, NBTReadOnly.EMPTY_NBT);
            case 1 -> new ByteTag(key, readByte());
            case 2 -> new ShortTag(key, readShort());
            case 3 -> new IntTag(key, readInt());
            case 4 -> new LongTag(key, readLong());
            case 5 -> new FloatTag(key, readFloat());
            case 6 -> new DoubleTag(key, readDouble());
            case 7 -> new ByteArrayTag(key, readBytes());
            case 8 -> new StringTag(key, readString());
            case 9 -> readList(key);
            case 10 -> new CompoundTag(key, readNBT());
            case 11 -> new IntArrayTag(key, readIntArray());
            case 12 -> new LongArrayTag(key, readLongArray());
            default -> null;
        };
    }

    public void readNBT(final NBT nbt) {
        while(index < buffer.length) {
            final byte tagId = readByte();
            if (tagId == 0) {
                break;
            }

            final String key = readString();
            final Tag tag = readTag(tagId, key);
            if (tag != null) {
                nbt.add(tag);
            }
        }
    }

    public NBT readNBT() {
        final NBT nbt = new NBTMap();
        readNBT(nbt);
        return nbt;
    }

    public void skipTo(final int index) {
        this.index = index;
    }
}
