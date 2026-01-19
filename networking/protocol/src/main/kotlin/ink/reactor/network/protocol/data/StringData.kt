package ink.reactor.network.protocol.data

import ink.reactor.network.protocol.data.VarInt.write
import io.netty.buffer.ByteBuf
import java.net.ProtocolException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object StringData {

    fun readFixedAscii(buf: ByteBuf, length: Int): String {
        val bytes = ByteArray(length)
        buf.getBytes(buf.readerIndex(), bytes)
        var end = 0
        while (end < length && bytes[end].toInt() != 0) {
            ++end
        }
        return String(bytes, 0, end, StandardCharsets.US_ASCII)
    }

    fun readFixed(buf: ByteBuf, length: Int): String {
        val bytes = ByteArray(length)
        buf.getBytes(buf.readerIndex(), bytes)
        var end = 0
        while (end < length && bytes[end].toInt() != 0) {
            ++end
        }
        return String(bytes, 0, end, StandardCharsets.UTF_8)
    }

    fun readVar(buf: ByteBuf, limit: Int): String {
        return readVar(buf, limit, StandardCharsets.UTF_8)
    }

    fun readVarAscii(buf: ByteBuf, limit: Int): String {
        return readVar(buf, limit, StandardCharsets.US_ASCII)
    }

    fun readVar(buf: ByteBuf): String {
        return readVar(buf, Short.MAX_VALUE.toInt(), StandardCharsets.UTF_8)
    }

    fun readVarAscii(buf: ByteBuf): String {
        return readVar(buf, Short.MAX_VALUE.toInt(), StandardCharsets.US_ASCII)
    }

    fun readVar(buf: ByteBuf, limit: Int, charset: Charset): String {
        val len: Int = VarInt.read(buf)
        if (len !in 0..limit) {
            throw ProtocolException("VarInt is too large. Expected 0->$limit, found $len")
        }

        val bytes = ByteArray(len)
        buf.readBytes(bytes)
        return String(bytes, charset)
    }

    fun writeFixedAscii(buf: ByteBuf, value: String?, length: Int) {
        if (value == null) {
            buf.writeZero(length)
            return
        }

        val bytes = value.toByteArray(StandardCharsets.US_ASCII)
        if (bytes.size > length) {
            throw ProtocolException("Fixed ASCII string exceeds length: " + bytes.size + " > " + length)
        }
        buf.writeBytes(bytes)
        buf.writeZero(length - bytes.size)
    }

    fun writeFixed(buf: ByteBuf, value: String?, length: Int) {
        if (value == null) {
            buf.writeZero(length)
            return
        }
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        if (bytes.size > length) {
            throw ProtocolException("Fixed UTF-8 string exceeds length: " + bytes.size + " > " + length)
        }
        buf.writeBytes(bytes)
        buf.writeZero(length - bytes.size)
    }

    fun writeVar(buf: ByteBuf, value: String, maxLength: Int) {
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        if (bytes.size > maxLength) {
            throw ProtocolException("String exceeds max bytes: " + bytes.size + " > " + maxLength)
        }
        write(buf, bytes.size)
        buf.writeBytes(bytes)
    }

    fun writeVarAscii(buf: ByteBuf, value: String, maxLength: Int) {
        val bytes = value.toByteArray(StandardCharsets.US_ASCII)
        if (bytes.size > maxLength) {
            throw ProtocolException("String exceeds max bytes: " + bytes.size + " > " + maxLength)
        }
        write(buf, bytes.size)
        buf.writeBytes(bytes)
    }

    fun utf8ByteLength(string: String): Int {
        var len = 0
        var i = 0
        while (i < string.length) {
            val c = string[i]
            if (c < '\u0080') {
                ++len
                ++i
                continue
            }
            if (c < '\u0800') {
                len += 2
                ++i
                continue
            }
            if (Character.isHighSurrogate(c)) {
                len += 4
                i += 2
                continue
            }
            len += 3
            ++i
        }
        return len
    }
}
