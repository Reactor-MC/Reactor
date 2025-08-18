package ink.reactor.protocol.api.player;

import java.util.Collection;
import java.util.UUID;

public record GameProfile(
    String name,
    UUID uuid,
    Collection<Property> properties
) {
    public record Property(String name, String value, String signature) {}
}
