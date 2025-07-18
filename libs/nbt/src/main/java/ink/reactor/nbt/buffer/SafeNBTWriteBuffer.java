package ink.reactor.nbt.buffer;

public final class SafeNBTWriteBuffer extends NBTWriteBuffer {

    public SafeNBTWriteBuffer() {
        super(32);
    }

    @Override
    public void writeByte(final byte value) {
        tryResize(1);
        super.writeByte(value);
    }

    @Override
    public void writeShort(final short value) {
        tryResize(2);
        super.writeShort(value);
    }

    @Override
    public void writeInt(final int value) {
        tryResize(4);
        super.writeInt(value);
    }

    @Override
    public void writeLong(final long value) {
        tryResize(8);
        super.writeLong(value);
    }

    @Override
    public void writeFloat(final float value) {
        tryResize(4);
        super.writeFloat(value);
    }

    @Override
    public void writeDouble(final double value) {
        tryResize(8);
        super.writeDouble(value);
    }

    @Override
    public void writeBytes(final byte[] bytes) {
        tryResize(bytes.length + 4);
        super.writeBytes(bytes);
    }

    @Override
    public void writeString(final byte[] bytes) {
        tryResize(2 + bytes.length);
        super.writeString(bytes);
    }
}
