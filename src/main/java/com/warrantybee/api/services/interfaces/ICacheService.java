package com.warrantybee.api.services.interfaces;

/**
 * Interface representing a generic cache service.
 * Provides methods to set, get, and delete cache entries.
 */
public interface ICacheService {

    /**
     * Sets a value in the cache for the specified key.
     *
     * @param key   The key under which the value will be stored.
     * @param value The value to store in the cache.
     */
    void set(String key, String value);

    /**
     * Sets a value in the cache for the specified key with an expiration time.
     *
     * @param key           The key under which the value will be stored.
     * @param value         The value to store in the cache.
     * @param expirySeconds The expiration time in seconds after which the key will be removed.
     */
    void set(String key, String value, int expirySeconds);

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be retrieved.
     * @return The value associated with the key, or null if the key does not exist.
     */
    String get(String key);

    /**
     * Deletes the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be deleted.
     */
    void delete(String key);
}