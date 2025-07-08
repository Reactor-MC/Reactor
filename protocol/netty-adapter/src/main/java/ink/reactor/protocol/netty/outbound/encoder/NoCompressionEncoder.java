package ink.reactor.protocol.netty.outbound.encoder;

import ink.reactor.protocol.api.buffer.DataSize;
import ink.reactor.protocol.api.buffer.writer.ExpectedSizeBuffer;
import ink.reactor.protocol.netty.outbound.PacketOutbound;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public final class NoCompressionEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        final PacketOutbound packet = (PacketOutbound)msg;
        final byte[] packetBuffer = packet.write();
        final int outboundIdLength = DataSize.varInt(packet.getId());
        final int packetLength = packetBuffer.length + outboundIdLength;

        final ExpectedSizeBuffer header = new ExpectedSizeBuffer(DataSize.varInt(packetLength) + outboundIdLength);

        header.writeVarInt(packetLength);
        header.writeVarInt(packet.getId());

        ctx.write(Unpooled.wrappedBuffer(header.buffer));
        ctx.write(Unpooled.wrappedBuffer(packetBuffer));
    }
}
