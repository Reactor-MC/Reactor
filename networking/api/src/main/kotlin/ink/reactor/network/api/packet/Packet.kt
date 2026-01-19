package ink.reactor.network.api.packet

import io.netty.buffer.ByteBuf

interface Packet {
    fun write(buf: ByteBuf)
    fun read(buf: ByteBuf)

    fun maxSize(): Int
    fun size(): Int

    fun id(): Int

    fun needCompression(): Boolean = false
}
