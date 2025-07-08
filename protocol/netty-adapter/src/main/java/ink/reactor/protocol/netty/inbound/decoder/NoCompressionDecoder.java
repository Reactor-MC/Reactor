package ink.reactor.protocol.netty.inbound.decoder;

import java.util.List;

import ink.reactor.kernel.Reactor;
import ink.reactor.protocol.netty.inbound.InboundPacket;
import ink.reactor.protocol.netty.inbound.PacketInData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class NoCompressionDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(final ChannelHandlerContext channelhandlercontext, final ByteBuf in, final List<Object> out) {
        if (in.readableBytes() < 1) {
            return;
        }

        in.markReaderIndex();
        final int packetLength = PacketInData.readVarIntSafely(in);

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
        final PacketInData packetInData = new PacketInData(packetData);
        final int id = packetInData.readVarInt();

        out.add(new InboundPacket(id, packetInData));
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        Reactor.getServer().getLogger().error("Error decoding packet", cause);
        ctx.close();
    }
}
