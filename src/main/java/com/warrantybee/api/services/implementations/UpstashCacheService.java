package com.warrantybee.api.services.implementations;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.services.interfaces.ICacheService;
import org.springframework.stereotype.Service;

/**
 * Redis cache service using Upstash REST API.
 * Supports basic set, get, and delete operations with optional TTL.
 */
@Service
public class UpstashCacheService implements ICacheService {

    private final String _upstashBaseUrl;
    private final String _upstashToken;
    private final HttpClient _httpClient;
    private final ObjectMapper _objectMapper;

    /**
     * Constructs the service with Upstash configuration.
     *
     * @param appConfig    application configuration
     * @param objectMapper Jackson object mapper
     */
    public UpstashCacheService(AppConfiguration appConfig, ObjectMapper objectMapper) {
        var cfg = appConfig.getUpstashConfiguration();
        this._upstashBaseUrl = cfg.getHost();
        this._upstashToken = cfg.getAccessToken();
        this._httpClient = HttpClient.newHttpClient();
        this._objectMapper = objectMapper;

        if (_upstashBaseUrl == null || _upstashToken == null) {
            throw new ConfigurationException("Upstash configuration (URL or token) is missing.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(String key, String value) {
        set(key, value, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void set(String key, String value, int expirySeconds) {
        if (key == null || key.isBlank()) {
            throw new InvalidInputException("Cache key cannot be null or blank.");
        }
        if (value == null) {
            throw new InvalidInputException("Cache value cannot be null.");
        }

        try {
            List<Object> command = new ArrayList<>();
            command.add("SET");
            command.add(key);
            command.add(value);

            if (expirySeconds > 0) {
                command.add("EX");
                command.add(expirySeconds);
            }

            String body = _objectMapper.writeValueAsString(command);
            _send(_upstashBaseUrl, body);

        } catch (IOException e) {
            Thread.currentThread().interrupt();
            throw new CacheException("Failed to set cache value for key: " + key, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String get(String key) {
        if (key == null || key.isBlank()) {
            throw new InvalidInputException("Cache key cannot be null or blank.");
        }

        try {
            List<Object> command = List.of("GET", key);
            String body = _objectMapper.writeValueAsString(command);

            HttpResponse<String> response = _send(_upstashBaseUrl, body);
            String responseBody = response.body();

            Map<String, Object> responseMap = _objectMapper.readValue(responseBody, new TypeReference<>() {});

            if (responseMap.containsKey("error")) {
                throw new CacheException("Upstash error: " + responseMap.get("error"));
            }

            Object result = responseMap.get("result");
            return (result != null) ? result.toString() : null;

        } catch (IOException e) {
            throw new CacheException("Failed to parse Upstash response for key: " + key, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(String key) {
        if (key == null || key.isBlank()) {
            throw new InvalidInputException("Cache key cannot be null or blank.");
        }

        try {
            List<Object> command = List.of("DEL", key);
            String body = _objectMapper.writeValueAsString(command);
            _send(_upstashBaseUrl, body);
        } catch (IOException e) {
            Thread.currentThread().interrupt();
            throw new CacheException("Failed to delete cache key: " + key, e);
        }
    }

    /**
     * Sends an HTTP POST request to Upstash.
     *
     * @param url  the Upstash endpoint
     * @param body the JSON request body
     * @return HttpResponse from Upstash
     */
    private HttpResponse<String> _send(String url, String body) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + _upstashToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = null;

        try {
            response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        int status = response.statusCode();

        if (status == 401) {
            throw new UnauthorizedAccessException("Invalid Upstash credentials or expired token.");
        }
        else if (status == 500) {
            throw new InternalServerErrorException("Upstash internal error: " + response.body());
        }
        else if (status >= 503) {
            throw new ServiceUnavailableException("Upstash service unavailable: " + response.body());
        }
        else if (status != 200) {
            throw new CacheException("Unexpected Upstash response (status " + status + "): " + response.body());
        }

        return response;
    }
}
