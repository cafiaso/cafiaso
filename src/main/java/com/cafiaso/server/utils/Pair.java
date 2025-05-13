package com.cafiaso.server.utils;

/**
 * A simple pair class to hold two related objects.
 *
 * @param <K> the type of the first object
 * @param <V> the type of the second object
 */
public record Pair<K, V>(K key, V value) {

}
