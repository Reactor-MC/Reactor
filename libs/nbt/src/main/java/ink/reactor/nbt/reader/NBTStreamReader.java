package ink.reactor.nbt.reader;

import ink.reactor.nbt.NBT;
import ink.reactor.nbt.buffer.NBTReadBuffer;
import ink.reactor.nbt.compression.CompressionUtils;

import java.io.File;
import java.io.IOException;

public class NBTStreamReader {

    public static NBT read(final File file) throws IOException {
        final byte[] uncompressedBytes = CompressionUtils.getUncompressedBytes(file);
        if (uncompressedBytes == null) {
            return null;
        }
        return NBTByteReader.read(new NBTReadBuffer(uncompressedBytes), true);
    }
}
