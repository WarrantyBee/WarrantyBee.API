package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.exceptions.CacheException;

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
     * @throws CacheException If an error occurs during the cache operation.
     */
    void set(String key, String value) throws CacheException;

    /**
     * Sets a value in the cache for the specified key with an expiration time.
     *
     * @param key           The key under which the value will be stored.
     * @param value         The value to store in the cache.
     * @param expirySeconds The expiration time in seconds after which the key will be removed.
     * @throws CacheException If an error occurs during the cache operation.
     */
    void set(String key, String value, int expirySeconds) throws CacheException;

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be retrieved.
     * @return The value associated with the key, or null if the key does not exist.
     * @throws CacheException If an error occurs during the cache operation.
     */
    String get(String key) throws CacheException;

    /**
     * Deletes the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be deleted.
     * @throws CacheException If an error occurs during the cache operation.
     */
    void delete(String key) throws CacheException;
}