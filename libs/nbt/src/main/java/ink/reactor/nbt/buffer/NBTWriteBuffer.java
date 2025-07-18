package ink.reactor.nbt.buffer;

import java.nio.charset.StandardCharsets;

public class NBTWriteBuffer {
    public byte[] buffer;
    public int index = 0;

    public NBTWriteBuffer(int size) {
        this.buffer = new byte[size];
    }

    public void tryResize(final int bytesToIncrease) {
        if (index + bytesToIncrease >= buffer.length) {
            final byte[] copy = new byte[buffer.length + bytesToIncrease];
            System.arraycopy(buffer, 0, copy, 0, buffer.length);
            this.buffer = copy;
        }
    }

    public void writeByte(byte value) {
        buffer[index++] = value;
    }

    public void writeShort(short value) {
        buffer[index++] = (byte)(value >>> 8);
        buffer[index++] = (byte)(value);
    }

    public void writeInt(int value) {
        buffer[index++] = (byte)(value >>> 24);
        buffer[index++] = (byte)(value >>> 16);
        buffer[index++] = (byte)(value >>> 8);
        buffer[index++] = (byte)(value);
    }

    public void writeLong(long value) {
        buffer[index++] = (byte)(value >>> 56);
        buffer[index++] = (byte)(value >>> 48);
        buffer[index++] = (byte)(value >>> 40);
        buffer[index++] = (byte)(value >>> 32);
        buffer[index++] = (byte)(value >>> 24);
        buffer[index++] = (byte)(value >>> 16);
        buffer[index++] = (byte)(value >>> 8);
        buffer[index++] = (byte)(value);
    }

    public void writeFloat(float value) {
        final int i = Float.floatToIntBits(value);
        buffer[index++] = (byte) (i >> 24);
        buffer[index++] = (byte) (i >> 16);
        buffer[index++] = (byte) (i >> 8);
        buffer[index++] = (byte) (i);
    }

    public void writeDouble(double value) {
        final long l = Double.doubleToLongBits(value);
        buffer[index++] = (byte) (l >> 56);
        buffer[index++] = (byte) (l >> 48);
        buffer[index++] = (byte) (l >> 40);
        buffer[index++] = (byte) (l >> 32);
        buffer[index++] = (byte) (l >> 24);
        buffer[index++] = (byte) (l >> 16);
        buffer[index++] = (byte) (l >> 8);
        buffer[index++] = (byte) (l);
    }

    public void writeBytes(byte[] bytes) {
        writeInt(bytes.length);
        System.arraycopy(bytes, 0, buffer, index, bytes.length);
        index += bytes.length;
    }

    public void writeString(final byte[] bytes) {
        int length = bytes.length;
        buffer[index++] = (byte)(length >>> 8);
        buffer[index++] = (byte)(length);

        System.arraycopy(bytes, 0, buffer, index, bytes.length);
        index += bytes.length;
    }

    public void writeString(String string) {
        writeString(string.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] compress() {
        if (buffer.length == index) {
            return buffer;
        }
        final byte[] compressedBuffer = new byte[index];
        System.arraycopy(buffer, 0, compressedBuffer, 0, index);
        return compressedBuffer;
    }
}
