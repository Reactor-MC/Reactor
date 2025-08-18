package ink.reactor.api.player;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@UtilityClass
public final class Players {
    private static final Collection<Player> PLAYERS = new CopyOnWriteArrayList<>();
    private static final Map<UUID, Player> PLAYERS_PER_UUID = new ConcurrentHashMap<>();

    public static Collection<Player> getPlayers() {
        return new ArrayList<>(PLAYERS);
    }

    public static void forEach(final Consumer<Player> consumer) {
        for (final Player player : PLAYERS) {
            consumer.accept(player);
        }
    }

    public static Player byUUID(final UUID uuid) {
        return PLAYERS_PER_UUID.get(uuid);
    }

    public static Player byName(final String name) {
        Player found = null;
        final String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;

        for (Player player : PLAYERS) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) break;
            }
        }
        return found;
    }

    public static Player byNameExact(final String name) {
        for (Player player : PLAYERS) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public static void registerPlayer(final Player player) {
        if (PLAYERS_PER_UUID.containsKey(player.getUUID())) {
            throw new IllegalStateException("Player is already registered");
        }
        PLAYERS_PER_UUID.put(player.getUUID(), player);
        PLAYERS.add(player);
    }

    public static void unregisterPlayer(final Player player) {
        PLAYERS_PER_UUID.remove(player.getUUID());
        PLAYERS.remove(player);
    }
}
