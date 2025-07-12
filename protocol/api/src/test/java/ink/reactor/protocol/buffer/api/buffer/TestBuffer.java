package ink.reactor.protocol.buffer.api.buffer;

import ink.reactor.protocol.api.buffer.DataSize;
import ink.reactor.protocol.api.buffer.reader.BufferReader;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.buffer.writer.DynamicSizeBuffer;
import ink.reactor.protocol.api.buffer.writer.ExpectedSizeBuffer;
import ink.reactor.protocol.api.buffer.writer.WriterBuffer;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuffer {

    @Test
    void testExpectedSizeBufferBasicOperations() {
        final WriterBuffer writer = new ExpectedSizeBuffer(128);

        writer.writeBoolean(true);
        writer.writeByte((byte) 0x7F);
        writer.writeShort(0xABCD);
        writer.writeInt(0x12345678);
        writer.writeLong(0x1122334455667788L);
        writer.writeFloat(3.14159f);
        writer.writeDouble(2.71828);
        writer.writeChar('X');

        final byte[] buffer = writer.compress();
        final ReaderBuffer reader = new BufferReader(buffer);

        assertTrue(reader.readBoolean());
        assertEquals((byte) 0x7F, reader.readByte());
        assertEquals((short) 0xABCD, reader.readShort());
        assertEquals(0x12345678, reader.readInt());
        assertEquals(0x1122334455667788L, reader.readLong());
        assertEquals(3.14159f, reader.readFloat(), 0.0001f);
        assertEquals(2.71828, reader.readDouble(), 0.0001);
        assertEquals('X', reader.readChar());
    }

    @Test
    void testDynamicSizeBufferResizing() {
        WriterBuffer writer = new DynamicSizeBuffer(4, 1.5f);

        writer.writeInt(1);
        writer.writeInt(2);
        writer.writeInt(3);

        byte[] buffer = writer.compress();
        assertEquals(12, buffer.length);

        ReaderBuffer reader = new BufferReader(buffer);
        assertEquals(1, reader.readInt());
        assertEquals(2, reader.readInt());
        assertEquals(3, reader.readInt());
    }

    @Test
    void testVarIntEncoding() {
        WriterBuffer writer = new ExpectedSizeBuffer(16);

        writer.writeVarInt(0);
        writer.writeVarInt(127);
        writer.writeVarInt(128);
        writer.writeVarInt(16383);
        writer.writeVarInt(16384);
        writer.writeVarInt(2097151);
        writer.writeVarInt(2097152);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        assertEquals(0, reader.readVarInt());
        assertEquals(127, reader.readVarInt());
        assertEquals(128, reader.readVarInt());
        assertEquals(16383, reader.readVarInt());
        assertEquals(16384, reader.readVarInt());
        assertEquals(2097151, reader.readVarInt());
        assertEquals(2097152, reader.readVarInt());
    }

    @Test
    void testStringEncoding() {
        String testString = "Hello world こんにちは世界";

        WriterBuffer writer = new ExpectedSizeBuffer(128);
        writer.writeString(testString);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        String decodedString = reader.readString();
        assertEquals(testString, decodedString);
    }

    @Test
    void testUUIDEncoding() {
        UUID testUUID = UUID.randomUUID();

        WriterBuffer writer = new ExpectedSizeBuffer(32);
        writer.writeUUID(testUUID);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        UUID decodedUUID = reader.readUUID();
        assertEquals(testUUID, decodedUUID);
    }

    @Test
    void testBitSetOperations() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(3);
        bitSet.set(5);
        bitSet.set(7);
        bitSet.set(64);

        WriterBuffer writer = new ExpectedSizeBuffer(32);
        writer.writeBitSet(bitSet);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        BitSet decodedBitSet = reader.readBitSet();
        assertEquals(bitSet, decodedBitSet);
    }

    @Test
    void testFixedBitSetOperations() {
        BitSet bitSet = new BitSet(16);
        bitSet.set(1);
        bitSet.set(3);
        bitSet.set(5);
        bitSet.set(7);
        bitSet.set(15);

        WriterBuffer writer = new ExpectedSizeBuffer(32);
        writer.writeBytes(bitSet.toByteArray()); // Simulamos writeFixedBitSet

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        BitSet decodedBitSet = reader.readFixedBitSet(16);
        for (int i = 0; i < 16; i++) {
            assertEquals(bitSet.get(i), decodedBitSet.get(i), "Bit " + i + " differs");
        }
    }

    @Test
    void testLongArrayOperations() {
        long[] testArray = {1L, 2L, 3L, Long.MAX_VALUE, Long.MIN_VALUE};

        WriterBuffer writer = new ExpectedSizeBuffer(128);
        writer.writeLongArray(testArray);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        long[] decodedArray = reader.readLongArray();
        assertArrayEquals(testArray, decodedArray);
    }

    @Test
    void testSkipAndBackOperations() {
        final int bufferSize = DataSize.INT * 4;
        WriterBuffer writer = new ExpectedSizeBuffer(bufferSize);

        writer.writeInt(1);
        writer.writeInt(2);

        final int initialIndex = writer.getIndex();
        writer.skip(DataSize.INT);
        writer.back(DataSize.INT);
        assertEquals(initialIndex, writer.getIndex());

        writer.skip(DataSize.INT);
        writer.writeInt(4);

        writer.back(DataSize.INT * 2);
        writer.writeInt(3);

        writer.setIndex(bufferSize);

        final byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        assertEquals(1, reader.readInt());
        assertEquals(2, reader.readInt());
        assertEquals(3, reader.readInt());
        assertEquals(4, reader.readInt());
    }

    @Test
    void testCharsOperations() {
        char[] testChars = {'H', 'e', 'l', 'l', 'o', ' ', '世', '界'};

        WriterBuffer writer = new ExpectedSizeBuffer(32);
        writer.writeChars(testChars);

        byte[] buffer = writer.compress();
        ReaderBuffer reader = new BufferReader(buffer);

        char[] decodedChars = reader.readChars(testChars.length);
        assertArrayEquals(testChars, decodedChars);
    }

    @Test
    void testIndexManagement() {
        WriterBuffer writer = new ExpectedSizeBuffer(32);

        assertEquals(0, writer.getIndex());
        writer.writeInt(123);
        assertEquals(4, writer.getIndex());
        writer.skip(4);
        assertEquals(8, writer.getIndex());
        writer.revertTo(4);
        assertEquals(4, writer.getIndex());
    }
}
