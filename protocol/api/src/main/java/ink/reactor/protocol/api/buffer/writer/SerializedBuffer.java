package ink.reactor.protocol.api.buffer.writer;

import ink.reactor.protocol.api.buffer.DataSize;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public final class SerializedBuffer {

    public static byte[] empty() {
        return ExpectedSizeBuffer.EMPTY;
    }

    public static byte[] from(final String string) {
        final byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        final ExpectedSizeBuffer expectedSizeBuffer = new ExpectedSizeBuffer(DataSize.prefixedBytes(stringBytes));
        expectedSizeBuffer.writeVarInt(stringBytes.length);
        expectedSizeBuffer.writeBytes(stringBytes);
        return expectedSizeBuffer.compress();
    }

    public static byte[] from(final boolean value) {
        return new byte[] { (value ? (byte)1 : 0) };
    }

    public static byte[] from(final byte value) {
        return new byte[] { value };
    }

    public static byte[] from(short value) {
        return new byte[] { (byte)(value >>> 8), (byte)(value) };
    }

    public static byte[] from(int value) {
        return new byte[] { (byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)(value) };
    }

    public static byte[] from(long value) {
        return new byte[] {
            (byte)(value >>> 56), (byte)(value >>> 48), (byte)(value >>> 40), (byte)(value >>> 32),
            (byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)(value)
        };
    }
}
