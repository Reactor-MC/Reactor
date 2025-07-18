package ink.reactor.nbt.reader;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTReadBuffer;
import ink.reactor.nbt.tags.TagId;

public final class NBTByteReader {

    public static NBT read(final NBTReadBuffer buffer, final boolean includeRootName) {
        if (buffer.readByte() != TagId.COMPOUND.byteValue) {
            return null;
        }
        if (includeRootName) {
            buffer.readString(); // Ignore root name
        }
        return buffer.readNBT();
    }
}
