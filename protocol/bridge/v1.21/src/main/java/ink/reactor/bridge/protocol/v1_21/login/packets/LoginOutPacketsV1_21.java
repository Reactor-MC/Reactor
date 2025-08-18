package ink.reactor.bridge.protocol.v1_21.login.packets;

import ink.reactor.bridge.protocol.v1_21.OutboundPackets;
import ink.reactor.protocol.api.buffer.DataSize;
import ink.reactor.protocol.api.buffer.writer.DynamicSizeBuffer;
import ink.reactor.protocol.api.buffer.writer.SerializedBuffer;
import ink.reactor.protocol.api.packet.CachedPacket;
import ink.reactor.protocol.api.packet.PacketOutbound;
import ink.reactor.protocol.api.packet.login.LoginOutPackets;
import ink.reactor.protocol.api.player.GameProfile;

import java.util.Collection;

public class LoginOutPacketsV1_21 implements LoginOutPackets {

    @Override
    public PacketOutbound createDisconnect(final String reason) {
        return new CachedPacket(OutboundPackets.LOGIN_DISCONNECT, SerializedBuffer.from(reason));
    }

    @Override
    public PacketOutbound createSuccess(final GameProfile gameProfile) {
        final DynamicSizeBuffer buffer = new DynamicSizeBuffer(DataSize.UUID + DataSize.string(gameProfile.name()));
        buffer.writeUUID(gameProfile.uuid());
        buffer.writeString(gameProfile.name());

        final Collection<GameProfile.Property> properties = gameProfile.properties();
        buffer.writeVarInt(properties.size());
        if (!properties.isEmpty()) {
            for (final GameProfile.Property property : properties) {
                buffer.writeString(property.name());
                buffer.writeString(property.value());

                if (property.signature() == null) {
                    buffer.writeBoolean(false);
                    continue;
                }
                buffer.writeString(property.signature());
            }
        }

        return new CachedPacket(OutboundPackets.LOGIN_FINISHED, buffer.compress());
    }
}
