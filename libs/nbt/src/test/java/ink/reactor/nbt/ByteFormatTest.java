package ink.reactor.nbt;

import ink.reactor.nbt.adapter.NBTMap;
import ink.reactor.nbt.buffer.NBTReadBuffer;
import ink.reactor.nbt.buffer.NBTWriteBuffer;
import ink.reactor.nbt.reader.NBTByteReader;
import ink.reactor.nbt.writer.NBTByteWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteFormatTest {

    @Test
    public void testByteWriter() {
        final NBTMap nbt = new NBTMap();
        nbt.addByte("byte", (byte)2);
        nbt.addBoolean("boolean", true);
        nbt.addIntArray("intArray", 2, 3, 5);

        final NBT subSection = new NBTMap();
        subSection.addFloat("float", 3.14F);

        nbt.addSection("subSection", subSection);

        final NBTWriteBuffer buffer = NBTByteWriter.writeNBT(nbt);
        final NBTReadBuffer readBuffer = new NBTReadBuffer(buffer.compress());
        final NBT nbt1 = NBTByteReader.read(readBuffer, false);
        Assertions.assertEquals(nbt, nbt1);
    }
}
