package ink.reactor.protocol.api.buffer.reader;

import java.util.BitSet;
import java.util.UUID;

/**
 * Interface for reading data from a binary buffer.
 * Provides methods to read various primitive types and common data structures
 * that were written using a WriterBuffer.
 */
public interface ReaderBuffer {

    /**
     * Reads a variable-length integer (VarInt) from the buffer.
     * @return the decoded integer value
     */
    int readVarInt();

    /**
     * Reads a byte array from the buffer.
     * @param length number of bytes to read
     * @return the byte array
     */
    byte[] readBytes(int length);

    /**
     * Reads a char array from the buffer.
     * @param length number of chars to read
     * @return the char array
     */
    char[] readChars(int length);

    /**
     * Reads an array of long values preceded by its length as VarInt.
     * @return the long array
     */
    long[] readLongArray();

    /**
     * Reads a boolean value from the buffer.
     * @return the boolean value
     */
    boolean readBoolean();

    /**
     * Reads a single byte from the buffer.
     * @return the byte value
     */
    byte readByte();

    /**
     * Reads an unsigned byte from the buffer.
     * @return the unsigned byte value as int
     */
    int readUnsignedByte();

    /**
     * Reads a short value from the buffer.
     * @return the short value
     */
    short readShort();

    /**
     * Reads a char value from the buffer.
     * @return the char value
     */
    char readChar();

    /**
     * Reads an integer value from the buffer.
     * @return the integer value
     */
    int readInt();

    /**
     * Reads a UUID from the buffer as two long values.
     * @return the UUID
     */
    UUID readUUID();

    /**
     * Reads a long value from the buffer.
     * @return the long value
     */
    long readLong();

    /**
     * Reads a float value from the buffer.
     * @return the float value
     */
    float readFloat();

    /**
     * Reads a double value from the buffer.
     * @return the double value
     */
    double readDouble();

    /**
     * Reads a UTF-8 encoded string preceded by its length as VarInt.
     * @return the decoded string
     */
    String readString();

    /**
     * Reads a UTF-8 encoded string with length check.
     * @param maxLength maximum allowed string length in bytes
     * @return the decoded string
     * @throws IllegalStateException if string exceeds maxLength
     */
    String readString(final int maxLength);

    /**
     * Reads a BitSet preceded by its length in longs as VarInt.
     * @return the BitSet
     */
    BitSet readBitSet();

    /**
     * Reads a fixed-size BitSet from the buffer.
     * @param size the number of bits in the BitSet
     * @return the BitSet
     */
    BitSet readFixedBitSet(int size);

    /**
     * Sets the read position to the specified absolute index.
     * @param index the absolute position to set
     */
    void skipTo(int index);

    /**
     * Gets the current read position in the buffer.
     * @return the current index
     */
    int getIndex();
}
