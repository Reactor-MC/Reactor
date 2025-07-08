package ink.reactor.protocol.api;

import java.util.Collection;

public interface PlayerConnection {

    void sendPacket(final Object packet);
    void sendPackets(final Collection<Object> packets);

    void sendNowPacket(final Object packet);
    void sendNowPackets(final Collection<Object> packets);
}
