package ink.reactor.protocol.api.buffer.writer;

import java.util.BitSet;
import java.util.UUID;

/**
 * Interface for writing data to a binary buffer.
 * Provides methods to write various primitive types and common data structures
 * in a compact binary format.
 */
public interface WriterBuffer {

    /**
     * Writes a variable-length integer (VarInt) to the buffer.
     * @param varInt the integer value to write
     */
    void writeVarInt(final int varInt);

    /**
     * Writes a byte array to the buffer.
     * @param bytes the byte array to write
     */
    void writeBytes(final byte[] bytes);

    /**
     * Writes a portion of a byte array to the buffer.
     * @param bytes the source byte array
     * @param length the number of bytes to write
     */
    void writeBytes(final byte[] bytes, final int length);

    /**
     * Writes a char array to the buffer.
     * @param chars the char array to write
     */
    void writeChars(final char[] chars);

    /**
     * Writes a boolean value to the buffer.
     * @param condition the boolean value to write
     */
    void writeBoolean(final boolean condition);

    /**
     * Writes a single byte to the buffer.
     * @param value the byte value to write
     */
    void writeByte(final byte value);

    /**
     * Writes a single byte to the buffer from an integer value.
     * @param value the integer value to write as byte
     */
    void writeByte(final int value);

    /**
     * Writes a short value to the buffer.
     * @param value the short value to write
     */
    void writeShort(final int value);

    /**
     * Writes a char value to the buffer.
     * @param character the char value to write
     */
    void writeChar(final char character);

    /**
     * Writes an integer value to the buffer.
     * @param value the integer value to write
     */
    void writeInt(final int value);

    /**
     * Writes a long value to the buffer.
     * @param value the long value to write
     */
    void writeLong(final long value);

    /**
     * Writes a float value to the buffer.
     * @param value the float value to write
     */
    void writeFloat(final float value);

    /**
     * Writes a double value to the buffer.
     * @param value the double value to write
     */
    void writeDouble(final double value);

    /**
     * Writes a UTF-8 encoded string to the buffer preceded by its length as VarInt.
     * @param string the string to write
     */
    void writeString(final String string);

    /**
     * Writes an array of long values to the buffer preceded by its length as VarInt.
     * @param longs the long array to write
     */
    void writeLongArray(final long[] longs);

    /**
     * Writes a UUID to the buffer as two long values.
     * @param uuid the UUID to write
     */
    void writeUUID(final UUID uuid);

    /**
     * Writes a BitSet to the buffer preceded by its length in longs as VarInt.
     * @param bitSet the BitSet to write
     */
    void writeBitSet(final BitSet bitSet);

    /**
     * Moves the write pointer backward without modifying data.
     * @param amountBytes number of bytes to move back
     */
    void back(int amountBytes);

    /**
     * Reverts the write pointer and clears the specified number of bytes.
     * @param index index to revert
     */
    void revertTo(final int index);

    /**
     * Advances the write pointer forward without writing data.
     * @param amountBytes number of bytes to skip
     */
    void skip(final int amountBytes);

    /**
     * Returns a compressed copy of the buffer containing only the written data.
     * @return a byte array containing the written data
     */
    byte[] compress();

    /**
     * Gets the current write position in the buffer.
     * @return the current index
     */
    int getIndex();

    void setIndex(int index);
}
