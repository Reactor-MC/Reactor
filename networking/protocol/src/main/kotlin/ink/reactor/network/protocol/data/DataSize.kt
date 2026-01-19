package ink.reactor.network.protocol.data

object DataSize {
    const val BYTE = 1
    const val SHORT = 2
    const val CHAR = 2
    const val INT = 4
    const val LONG = 8
    const val UUID = 16

    fun varInt(value: Int): Int {
        return VarInt.size(value);
    }

    fun string(value: String): Int {
        val length = StringData.utf8ByteLength(value)
        return varInt(length) + length;
    }
}
