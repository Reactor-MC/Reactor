package ink.reactor.protocol.netty.inbound;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

public record PacketInData(ByteBuf buffer) {

    public int readVarInt() {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = buffer.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 0x80) == 128);
        return i;
    }

    public String readString() {
        return readString(Short.MAX_VALUE);
    }

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

    public BitSet readFixedBitSet(final int a) {
        final byte[] bytes = new byte[-Math.floorDiv(-a, 8)];
        buffer.readBytes(bytes);
        return BitSet.valueOf(bytes);
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
