package com.warrantybee.api.services.interfaces;

import java.io.IOException;

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
     * @throws IOException          If an I/O error occurs during the operation.
     * @throws InterruptedException If the operation is interrupted.
     */
    void set(String key, String value) throws IOException, InterruptedException;

    /**
     * Sets a value in the cache for the specified key with an expiration time.
     *
     * @param key           The key under which the value will be stored.
     * @param value         The value to store in the cache.
     * @param expirySeconds The expiration time in seconds after which the key will be removed.
     * @throws IOException          If an I/O error occurs during the operation.
     * @throws InterruptedException If the operation is interrupted.
     */
    void set(String key, String value, int expirySeconds) throws IOException, InterruptedException;

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be retrieved.
     * @return The value associated with the key, or null if the key does not exist.
     * @throws IOException          If an I/O error occurs during the operation.
     * @throws InterruptedException If the operation is interrupted.
     */
    String get(String key) throws IOException, InterruptedException;

    /**
     * Deletes the value associated with the specified key from the cache.
     *
     * @param key The key whose value needs to be deleted.
     * @throws IOException          If an I/O error occurs during the operation.
     * @throws InterruptedException If the operation is interrupted.
     */
    void delete(String key) throws IOException, InterruptedException;
}
