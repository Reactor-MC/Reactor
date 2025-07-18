package ink.reactor.nbt.writer;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.buffer.SafeNBTWriteBuffer;
import ink.reactor.nbt.tags.TagId;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NBTByteWriter {

    private static NBTWriteBuffer newBuffer(final NBT nbt) {
        final SafeNBTWriteBuffer safeNBTWriteBuffer = new SafeNBTWriteBuffer();
        safeNBTWriteBuffer.buffer = new byte[nbt.getTags().size() * 32];
        return safeNBTWriteBuffer;
    }

    public static byte[] writeTags(final NBT nbt) {
        final NBTWriteBuffer buffer = newBuffer(nbt);
        nbt.write(buffer);
        return buffer.compress();
    }

    public static byte[] writeNBT(final NBT nbt, final boolean addRootName) {
        final NBTWriteBuffer buffer = newBuffer(nbt);
        writeNBT(nbt, buffer, addRootName);
        return buffer.compress();
    }

    public static void writeNBT(final NBT nbt, final NBTWriteBuffer buffer, final boolean addRootName) {
        buffer.writeByte(TagId.COMPOUND.byteValue);

        if (addRootName) {
            buffer.writeShort((short)0); // Root name
        }

        nbt.write(buffer);

        buffer.writeByte(TagId.END.byteValue);
    }

    public static void writeNBT(final NBT nbt, final NBTWriteBuffer buffer) {
        buffer.writeByte(TagId.COMPOUND.byteValue);
        nbt.write(buffer);
        buffer.writeByte(TagId.END.byteValue);
    }

    public static NBTWriteBuffer writeNBT(final NBT nbt) {
        final NBTWriteBuffer buffer = newBuffer(nbt);
        writeNBT(nbt, buffer);
        return buffer;
    }
}
