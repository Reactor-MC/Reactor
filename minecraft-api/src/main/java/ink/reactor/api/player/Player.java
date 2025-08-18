package ink.reactor.api.player;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.player.GameProfile;

import java.util.UUID;

public interface Player {
    GameProfile getGameProfile();
    PlayerConnection getConnection();

    String getName();
    UUID getUUID();
}
