package ink.reactor.network.protocol.data

import io.netty.buffer.ByteBuf

/**
 * Utility object that converts between **IEEE-754 half-precision floating point**
 * (16-bit, a.k.a. Float16) and standard Java/Kotlin **Float** (32-bit).
 *
 * It also provides helpers to read and write half-floats in **little-endian**
 * format using Netty’s `ByteBuf`.
 *
 * ## IEEE-754 Half-Precision Layout
 *
 * | Field     | Bits | Description |
 * |-----------|------|-------------|
 * | Sign      | 1    | 0 = positive, 1 = negative |
 * | Exponent  | 5    | Exponent with a bias of 15 |
 * | Mantissa  | 10   | Fraction (implicit leading 1 for normalized values) |
 *
 * ### Special values
 * - Exponent = 0 → zero or subnormal numbers
 * - Exponent = 31 → infinity or NaN
 */

object Float16 {

    fun readHalfLE(buf: ByteBuf): Float {
        return halfToFloat(buf.readShortLE())
    }

    fun writeHalfLE(buf: ByteBuf, value: Float) {
        buf.writeShortLE(floatToHalf(value).toInt())
    }

    fun halfToFloat(half: Short): Float {
        val h = half.toInt() and 0xFFFF
        val sign = (h ushr 15) and 1
        var exp = (h ushr 10) and 0x1F
        var mant = h and 0x3FF

        if (exp == 0) {
            if (mant == 0) {
                return if (sign == 0) 0.0f else -0.0f
            }
            exp = 1
            while ((mant and 0x400) == 0) {
                mant = mant shl 1
                exp--
            }
            mant = mant and 0x3FF
        } else if (exp == 31) {
            return if (mant == 0) {
                if (sign == 0) Float.POSITIVE_INFINITY else Float.NEGATIVE_INFINITY
            } else {
                Float.NaN
            }
        }

        val floatBits = (sign shl 31) or ((exp + 112) shl 23) or (mant shl 13)
        return Float.fromBits(floatBits)
    }

    fun floatToHalf(f: Float): Short {
        val bits = f.toRawBits()
        val sign = (bits ushr 16) and 0x8000
        var value = (bits and Int.MAX_VALUE) + 0x1000

        if (value >= 0x47800000) {
            if ((bits and Int.MAX_VALUE) >= 0x47800000) {
                if (value < 0x7F800000) {
                    return (sign or 0x7C00).toShort()
                }
                return (sign or 0x7C00 or ((bits and 0x007FFFFF) ushr 13)).toShort()
            }
            return (sign or 0x7BFF).toShort()
        }
        if (value >= 0x38800000) {
            return (sign or ((value - 0x38000000) ushr 13)).toShort()
        }
        if (value < 0x33000000) {
            return sign.toShort()
        }
        value = (bits and Int.MAX_VALUE) ushr 23
        return (sign or (((bits and 0x7FFFFF) or 0x800000) + (0x800000 ushr (value - 102)) ushr (126 - value))).toShort()
    }
}
