package ink.reactor.protocol.api.packet.login;

import ink.reactor.protocol.api.packet.PacketOutbound;
import ink.reactor.protocol.api.player.GameProfile;

public interface LoginOutPackets {
    PacketOutbound createDisconnect(String reason);
    PacketOutbound createSuccess(final GameProfile gameProfile);
}
