package ink.reactor.protocol.api.buffer.writer;

import ink.reactor.protocol.api.buffer.DataSize;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.UUID;

/*
 * A slow but safe alternative to ExpectedSizeBuffer
 * This alternative don't throw ArrayIndexOutOfBounds if
 * you write more data than the initial size (Automatic resize the buffer)
 */
@Getter
public final class DynamicSizeBuffer implements WriterBuffer {

    @Setter
    private ExpectedSizeBuffer currentBuffer;
    private final float resizeFactor;

    public DynamicSizeBuffer(int initialSize) {
        this.currentBuffer = new ExpectedSizeBuffer(initialSize);
        this.resizeFactor = 1.5f;
    }

    public DynamicSizeBuffer(int initialSize, float resizeFactor) {
        this.currentBuffer = new ExpectedSizeBuffer(initialSize);
        this.resizeFactor = resizeFactor;
    }

    public void tryResize(final int amountToAdd) {
        if (currentBuffer.index + amountToAdd >= currentBuffer.buffer.length) {
            final int newSize = (int)((currentBuffer.buffer.length + amountToAdd) * resizeFactor);
            final int currentIndex = currentBuffer.index;

            final byte[] copy = new byte[newSize];
            System.arraycopy(currentBuffer.buffer, 0, copy, 0, currentBuffer.buffer.length);
            this.currentBuffer = new ExpectedSizeBuffer(copy);
            this.currentBuffer.index = currentIndex;
        }
    }

    @Override
    public void writeVarInt(final int i) {
        tryResize(DataSize.varInt(i));
        currentBuffer.writeVarInt(i);
    }

    @Override
    public void writeBytes(final byte[] bytes) {
        tryResize(bytes.length);
        currentBuffer.writeBytes(bytes);
    }

    @Override
    public void writeBytes(final byte[] bytes, final int length) {
        tryResize(length);
        currentBuffer.writeBytes(bytes, length);
    }

    @Override
    public void writeChars(final char[] chars) {
        tryResize(chars.length * 2);
        currentBuffer.writeChars(chars);
    }

    @Override
    public void writeBoolean(final boolean v) {
        tryResize(DataSize.BOOLEAN);
        currentBuffer.writeBoolean(v);
    }

    @Override
    public void writeByte(final byte v) {
        tryResize(DataSize.BYTE);
        currentBuffer.writeByte(v);
    }

    @Override
    public void writeByte(final int v) {
        tryResize(DataSize.BYTE);
        currentBuffer.writeByte(v);
    }

    @Override
    public void writeShort(final int v) {
        tryResize(DataSize.SHORT);
        currentBuffer.writeShort(v);
    }

    @Override
    public void writeChar(final char character) {
        tryResize(DataSize.SHORT);
        currentBuffer.writeChar(character);
    }

    @Override
    public void writeInt(final int v) {
        tryResize(DataSize.INT);
        currentBuffer.writeInt(v);
    }

    @Override
    public void writeLong(final long v) {
        tryResize(DataSize.LONG);
        currentBuffer.writeLong(v);
    }

    @Override
    public void writeFloat(final float v) {
        tryResize(DataSize.FLOAT);
        currentBuffer.writeFloat(v);
    }

    @Override
    public void writeDouble(final double v) {
        tryResize(DataSize.DOUBLE);
        currentBuffer.writeDouble(v);
    }

    @Override
    public void writeUUID(final UUID uuid) {
        tryResize(DataSize.UUID);
        currentBuffer.writeUUID(uuid);
    }

    @Override
    public void writeBitSet(final BitSet bitSet) {
        final long[] bitSetArray = bitSet.toLongArray();
        tryResize(DataSize.varInt(bitSetArray.length) + (DataSize.LONG * bitSetArray.length));

        currentBuffer.writeVarInt(bitSetArray.length);
        for (final long value : bitSetArray) {
            currentBuffer.writeLong(value);
        }
    }

    @Override
    public void writeString(final String string) {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        tryResize(bytes.length + DataSize.varInt(bytes.length));
        currentBuffer.writeVarInt(bytes.length);
        currentBuffer.writeBytes(bytes);
    }

    @Override
    public void writeLongArray(final long[] longs) {
        tryResize(DataSize.varInt(longs.length) + DataSize.LONG * longs.length);
        currentBuffer.writeLongArray(longs);
    }

    @Override
    public void back(final int amountBytes) {
        currentBuffer.back(amountBytes);
    }

    @Override
    public void revertTo(final int index) {
        currentBuffer.revertTo(index);
    }

    @Override
    public void skip(final int amountBytes) {
        currentBuffer.skip(amountBytes);
    }

    @Override
    public byte[] compress() {
        return currentBuffer.compress();
    }

    @Override
    public int getIndex() {
        return currentBuffer.getIndex();
    }

    @Override
    public void setIndex(final int index) {
        currentBuffer.setIndex(index);
    }
}
