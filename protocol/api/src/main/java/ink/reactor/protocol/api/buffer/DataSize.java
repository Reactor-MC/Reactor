package ink.reactor.protocol.api.buffer;


public final class DataSize {
    public static final int
        UUID = 16,
        LONG = 8,
        BOOLEAN = 1,
        BYTE = 1,
        SHORT = 2,
        INT = 4,
        FLOAT = 4,
        DOUBLE = 8;

    /**
     * Only works for utf8 strings. UTF16 or other can return a fake value
     * If you want use utf16, get bytes of the string and use prefixedBytes
     * @param string utf8
     * @return size of string with prefixed length
     */
    public static int string(final String string) {
        final int length = string.length();
        return varInt(length) + length;
    }

    public static int prefixedBytes(final byte[] bytes) {
        return varInt(bytes.length) + bytes.length;
    }

    public static int stringArray(final String[] array) {
        int size = varInt(array.length);
        for (final String value : array) {
            size += string(value);
        }
        return size;
    }

    /*
     *  Value       Hex bytes                  Decimal bytes
     *  1           | 0x01                     | 1
     *  127         | 0x7f                     | 127
     *  128         | 0x80 0x01                | 128 1
     *  255         | 0xff 0x01                | 255 1
     *  25565       | 0xdd 0xc7 0x01           | 221 199 1
     *  2097151     | 0xff 0xff 0x7f           | 255 255 127
     *  2147483647  | 0xff 0xff 0xff 0xff 0x07 | 255 255 255 255 7
     *  -1          | 0xff 0xff 0xff 0xff 0x0f | 255 255 255 255 15
     *  -2147483648 | 0x80 0x80 0x80 0x80 0x08 | 128 128 128 128 8
     */
    public static int varInt(final int i) {
        if (i < 0) { // Negative numbers use 5 bytes
            return 5;
        }
        if (i < (1 << 7)) return 1;
        if (i < (1 << 14)) return 2;
        if (i < (1 << 21)) return 3;
        if (i < (1 << 28)) return 4;
        return 5;
    }

    public static int varLongSize(final long value) {
        if (value < 0) return 10; // Negative numbers use 10 bytes

        if (value < (1L << 7)) return 1;
        if (value < (1L << 14)) return 2;
        if (value < (1L << 21)) return 3;
        if (value < (1L << 28)) return 4;
        if (value < (1L << 35)) return 5;
        if (value < (1L << 42)) return 6;
        if (value < (1L << 49)) return 7;
        if (value < (1L << 56)) return 8;
        return 9;
    }
}