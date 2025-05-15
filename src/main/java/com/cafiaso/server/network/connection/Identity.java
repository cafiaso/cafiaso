package com.cafiaso.server.network.connection;

import java.util.UUID;

/**
 * Represents a player identity.
 *
 * @param name the name of the player
 * @param id   the unique identifier of the player
 */
public record Identity(String name, UUID id) {

}
