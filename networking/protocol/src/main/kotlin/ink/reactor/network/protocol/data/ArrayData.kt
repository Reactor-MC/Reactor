package ink.reactor.network.protocol.data

import io.netty.buffer.ByteBuf

object ArrayData {

    fun readBytes(buf: ByteBuf, length: Int): ByteArray {
        val array = ByteArray(length)
        buf.readBytes(array)
        return array
    }

    fun readShorts(buf: ByteBuf, length: Int): ShortArray {
        val result = ShortArray(length)
        for (i in 0..<length) {
            result[i] = buf.readShortLE()
        }
        return result
    }

    fun readFloats(buf: ByteBuf, length: Int): FloatArray {
        val result = FloatArray(length)
        for (i in 0..<length) {
            result[i] = buf.readFloatLE()
        }
        return result
    }
}
