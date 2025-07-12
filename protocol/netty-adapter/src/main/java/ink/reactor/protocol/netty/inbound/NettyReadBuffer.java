package ink.reactor.protocol.netty.inbound;

import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.UUID;

public record NettyReadBuffer(ByteBuf buffer) implements ReaderBuffer {

    @Override
    public int readVarInt() {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = buffer.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new DecoderException("VarInt too big");
        } while ((b0 & 0x80) == 128);
        return i;
    }

    @Override
    public byte[] readBytes(final int length) {
        final byte[] bytes = new byte[length];
        buffer.getBytes(getIndex(), bytes);
        return bytes;
    }

    @Override
    public char[] readChars(final int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = buffer.readChar();
        }
        return chars;
    }

    @Override
    public long[] readLongArray() {
        final int length = readVarInt();
        if (length < 0) {
            throw new DecoderException("Array cannot have length less than 0.");
        }
        long[] l = new long[length];
        for (int index = 0; index < length; index++) {
            l[index] = readLong();
        }
        return l;
    }

    @Override
    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public int readUnsignedByte() {
        return buffer.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return buffer.readShort();
    }

    @Override
    public char readChar() {
        return buffer.readChar();
    }

    @Override
    public int readInt() {
        return buffer.readInt();
    }

    @Override
    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    @Override
    public long readLong() {
        return buffer.readLong();
    }

    @Override
    public float readFloat() {
        return buffer.readFloat();
    }

    @Override
    public double readDouble() {
        return buffer.readDouble();
    }

    @Override
    public String readString() {
        return readString(Short.MAX_VALUE);
    }

    @Override
    public String readString(final int maxLength) {
        final int stringLength = this.readVarInt();
        if (stringLength > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + stringLength + " > " + (stringLength * 4) + ")");
        }
        if (stringLength < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }

        final String string = buffer.toString(buffer.readerIndex(), stringLength, StandardCharsets.UTF_8);
        buffer.readerIndex(buffer.readerIndex() + stringLength);

        if (string.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + stringLength + " > " + stringLength + ")");
        }

        return string;
    }

    @Override
    public BitSet readBitSet() {
        final int length = readVarInt();
        final long[] data = new long[length];
        for (int i = 0; i < length; i++) {
            data[i] = readLong();
        }
        return BitSet.valueOf(data);
    }

    @Override
    public BitSet readFixedBitSet(final int size) {
        final byte[] bytes = new byte[-Math.floorDiv(-size, 8)];
        buffer.readBytes(bytes);
        return BitSet.valueOf(bytes);
    }

    @Override
    public void skipTo(final int index) {
        buffer.readerIndex(index + buffer.readerIndex());
    }

    @Override
    public int getIndex() {
        return buffer.readerIndex();
    }

    public static int readVarIntSafely(final ByteBuf buf) {
        int numRead = 0;
        int result = 0;

        while (numRead < 5) {
            if (!buf.isReadable()) {
                buf.clear();
                return -1;
            }

            byte read = buf.readByte();
            int value = (read & 0x7F);
            result |= (value << (7 * numRead));

            numRead++;

            if ((read & 0x80) != 0x80) {
                return result;
            }
        }
        throw new RuntimeException("VarInt is too big");
    }
}
