package com.cafiaso.server.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the players connected to the server.
 */
public class PlayerManager {

    private final Map<UUID, Identity> players = new HashMap<>();

    public Collection<Identity> getOnlinePlayers() {
        return players.values();
    }
}
