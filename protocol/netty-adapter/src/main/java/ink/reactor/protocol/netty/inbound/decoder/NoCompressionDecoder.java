package ink.reactor.protocol.netty.inbound.decoder;

import java.util.List;

import ink.reactor.kernel.Reactor;
import ink.reactor.protocol.netty.inbound.InboundPacket;
import ink.reactor.protocol.netty.inbound.NettyReadBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class NoCompressionDecoder extends ByteToMessageDecoder {
    private static final RuntimeException VARINT_TOO_BIG = new RuntimeException("VarInt too big");

    @Override
    protected void decode(final ChannelHandlerContext channelhandlercontext, final ByteBuf in, final List<Object> out) {
        if (in.readableBytes() < 1) {
            return;
        }

        in.markReaderIndex();
        final int packetLength = readRawVarInt21(in);

        if (packetLength == -1) {
            in.resetReaderIndex();
            return;
        }

        if (packetLength <= 0) {
            throw new IllegalArgumentException("Invalid packet length: " + packetLength);
        }

        if (in.readableBytes() < packetLength) {
            in.resetReaderIndex();
            return;
        }

        final ByteBuf packetData = in.readBytes(packetLength);
        final NettyReadBuffer packetInData = new NettyReadBuffer(packetData);
        final int id = packetInData.readVarInt();

        out.add(new InboundPacket(id, packetInData));
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        Reactor.get().logger().error("Error decoding packet", cause);
        ctx.close();
    }

    /**
     * Reads a VarInt from the buffer of up to 21 bits in size.
     *
     * @param buffer the buffer to read from
     * @return the VarInt decoded, {@code 0} if no varint could be read
     * @throws RuntimeException if the VarInt is too big to be decoded
     */
    private static int readRawVarInt21(ByteBuf buffer) {
        if (buffer.readableBytes() < 4) {
            // we don't have enough that we can read a potentially full varint, so fall back to
            // the slow path.
            return readRawVarintSmallBuf(buffer);
        }
        int wholeOrMore = buffer.getIntLE(buffer.readerIndex());

        // take the last three bytes and check if any of them have the high bit set
        int atStop = ~wholeOrMore & 0x808080;
        if (atStop == 0) {
            // all bytes have the high bit set, so the varint we are trying to decode is too wide
            throw VARINT_TOO_BIG;
        }

        int bitsToKeep = Integer.numberOfTrailingZeros(atStop) + 1;
        buffer.skipBytes(bitsToKeep >> 3);

        // remove all bits we don't need to keep, a trick from
        // https://github.com/netty/netty/pull/14050#issuecomment-2107750734:
        //
        // > The idea is that thisVarintMask has 0s above the first one of firstOneOnStop, and 1s at
        // > and below it. For example if firstOneOnStop is 0x800080 (where the last 0x80 is the only
        // > one that matters), then thisVarintMask is 0xFF.
        //
        // this is also documented in Hacker's Delight, section 2-1 "Manipulating Rightmost Bits"
        int preservedBytes = wholeOrMore & (atStop ^ (atStop - 1));

        // merge together using this trick: https://github.com/netty/netty/pull/14050#discussion_r1597896639
        preservedBytes = (preservedBytes & 0x007F007F) | ((preservedBytes & 0x00007F00) >> 1);
        preservedBytes = (preservedBytes & 0x00003FFF) | ((preservedBytes & 0x3FFF0000) >> 2);
        return preservedBytes;
    }

    private static int readRawVarintSmallBuf(ByteBuf buffer) {
        if (!buffer.isReadable()) {
            return 0;
        }
        buffer.markReaderIndex();

        byte tmp = buffer.readByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result = tmp & 0x7F;
        if (!buffer.isReadable()) {
            buffer.resetReaderIndex();
            return 0;
        }
        if ((tmp = buffer.readByte()) >= 0) {
            return result | tmp << 7;
        }
        result |= (tmp & 0x7F) << 7;
        if (!buffer.isReadable()) {
            buffer.resetReaderIndex();
            return 0;
        }
        if ((tmp = buffer.readByte()) >= 0) {
            return result | tmp << 14;
        }
        return result | (tmp & 0x7F) << 14;
    }
}
