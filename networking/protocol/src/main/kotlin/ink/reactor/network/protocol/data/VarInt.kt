package ink.reactor.network.protocol.data

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderException
import kotlin.math.ceil

/**
 * # What even is a VarInt?
 * A VarInt is a variable-length integer encoding where smaller values use fewer bytes.
 * This implementation follows the classic **base-128 VarInt** format used by
 * Minecraft and similar network protocols.
 *
 * Each byte encodes:
 * - The **7 least significant bits** as data
 * - The **most significant bit (0x80)** as a continuation flag indicating that
 *   another byte follows
 * The least significant 7-bit group is written first, followed by increasingly
 * more significant groups. Because of this, VarInts are effectively **little-endian**,
 * although the groups are 7 bits wide rather than full bytes.
 *
 * VarInts are never longer than **5 bytes** for 32-bit integers
 * (VarLongs are never longer than 10 bytes).
 *
 * This implementation does **NOT** use ZigZag encoding as [Protocol Buffer Varints](https://protobuf.dev/programming-guides/encoding/#varints):
 * negative numbers are encoded using two's complement and therefore always occupy
 * the maximum length (5 bytes for {@code Int}).
 *
 * ### Example table
 *
 * | Value          | Hex bytes                  | Decimal bytes
 * |----------------|----------------------------|-------------------------
 * | 1              |0x01                        | 1
 * | 127            |0x7F                        | 127
 * | 128            |0x80 0x01                   | 128 1
 * | 255            |0xFF 0x01                   | 255 1
 * | 25565          |0xDD 0xC7 0x01              | 221 199 1
 * | 2,097,151      |0xFF 0xFF 0x7F              | 255 255 127
 * | 2,147,483,647  |0xFF 0xFF 0xFF 0xFF 0x07    | 255 255 255 255 7
 * | -1             |0xFF 0xFF 0xFF 0xFF 0x0F    | 255 255 255 255 15
 * | -2,147,483,648 |0x80 0x80 0x80 0x80 0x08    | 128 128 128 128 8
 */
object VarInt {

    private val VAR_INT_BUFFER_EMPTY: DecoderException = DecoderException("Bad VarInt decoded. Buffer is empty");
    private val VAR_INT_EMPTY: DecoderException = DecoderException("Bad VarInt decoded. Size is higher tan 5 bytes");
    private const val MAXIMUM_VAR_INT_SIZE: Int = 5
    private val VAR_INT_LENGTHS: IntArray = IntArray(33)

    init {
        for (i in VAR_INT_LENGTHS.indices) {
            VAR_INT_LENGTHS[i] = ceil((31.0 - (i - 1)) / 7.0).toInt()
        }
        VAR_INT_LENGTHS[32] = 1; // Special case for the number 0.
    }

    fun size(value: Int): Int {
        return VAR_INT_LENGTHS[Integer.numberOfLeadingZeros(value)];
    }

    fun length(buf: ByteBuf): Int {
        val pos = buf.readerIndex()
        while (pos < buf.writerIndex()) {
            if ((buf.readByte().toInt() and 0x80) == 0) {
                return pos
            }
            if (pos <= 5) continue
            return -1
        }
        return -1
    }

    fun read(buf: ByteBuf): Int {
        val readable = buf.readableBytes()
        if (readable == 0) {
            // special case for empty buffer
            throw VAR_INT_BUFFER_EMPTY
        }

        // we can read at least one byte, and this should be a common case
        var k = buf.readByte().toInt()
        if ((k and 0x80) != 128) {
            return k
        }

        // in case decoding one byte was not enough, use a loop to decode up to the next 4 bytes
        val maxRead = MAXIMUM_VAR_INT_SIZE.coerceAtMost(readable)
        var i = k and 0x7F
        for (j in 1..<maxRead) {
            k = buf.readByte().toInt()
            i = i or ((k and 0x7F) shl j * 7)
            if ((k and 0x80) != 128) {
                return i
            }
        }
        throw VAR_INT_EMPTY
    }

    fun write(buf: ByteBuf, value: Int) {
        // Peel the one and two byte count cases explicitly as they are the most common VarInt sizes
        // that the proxy will write, to improve inlining.
        if ((value and (-0x1 shl 7)) == 0) {
            buf.writeByte(value)
        } else if ((value and (-0x1 shl 14)) == 0) {
            val w = (value and 0x7F or 0x80) shl 8 or (value ushr 7)
            buf.writeShort(w)
        } else {
            writeFull(buf, value)
        }
    }

    private fun writeFull(buf: ByteBuf, value: Int) {
        // See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
        // This essentially is an unrolled version of the "traditional" VarInt encoding.

        if ((value and (-0x1 shl 7)) == 0) {
            buf.writeByte(value)
        } else if ((value and (-0x1 shl 14)) == 0) {
            val w = (value and 0x7F or 0x80) shl 8 or (value ushr 7)
            buf.writeShort(w)
        } else if ((value and (-0x1 shl 21)) == 0) {
            val w = (value and 0x7F or 0x80) shl 16 or (((value ushr 7) and 0x7F or 0x80) shl 8) or (value ushr 14)
            buf.writeMedium(w)
        } else if ((value and (-0x1 shl 28)) == 0) {
            val w = ((value and 0x7F or 0x80) shl 24 or (((value ushr 7) and 0x7F or 0x80) shl 16)
                or (((value ushr 14) and 0x7F or 0x80) shl 8) or (value ushr 21))
            buf.writeInt(w)
        } else {
            val w = ((value and 0x7F or 0x80) shl 24 or (((value ushr 7) and 0x7F or 0x80) shl 16
                ) or (((value ushr 14) and 0x7F or 0x80) shl 8) or ((value ushr 21) and 0x7F or 0x80))
            buf.writeInt(w)
            buf.writeByte(value ushr 28)
        }
    }
}
