package com.warrantybee.api.services;

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
import com.warrantybee.api.services.interfaces.ICacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Redis cache service using Upstash REST API.
 * Supports basic set, get, and delete operations with optional TTL.
 */
@Service
public class UpstashCacheService implements ICacheService {

    private static final Logger logger = LoggerFactory.getLogger(UpstashCacheService.class);

    private final String upstashBaseUrl;
    private final String upstashToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // <-- ADDED: For safe JSON handling

    /**
     * Constructs the service with Upstash configuration.
     *
     * @param appConfig    application configuration
     * @param objectMapper Spring's injected Jackson mapper
     */
    public UpstashCacheService(AppConfiguration appConfig, ObjectMapper objectMapper) { // <-- UPDATED
        var cfg = appConfig.getUpstashConfiguration();
        this.upstashBaseUrl = cfg.getHost();
        this.upstashToken = cfg.getAccessToken();
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper; // <-- ADDED

        if (upstashBaseUrl == null || upstashToken == null) {
            throw new IllegalStateException("Upstash URL or token not set in configuration");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(String key, String value) throws IOException, InterruptedException {
        set(key, value, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void set(String key, String value, int expirySeconds) throws IOException, InterruptedException {
        String url = upstashBaseUrl;
        List<Object> command = new ArrayList<>();
        command.add("SET");
        command.add(key);
        command.add(value);

        if (expirySeconds > 0) {
            command.add("EX");
            command.add(expirySeconds);
        }

        String body = objectMapper.writeValueAsString(command);
        sendRequest(url, body);
    }

    /** {@inheritDoc} */
    @Override
    public String get(String key) throws IOException, InterruptedException {
        String url = upstashBaseUrl;

        List<Object> command = List.of("GET", key);
        String body = objectMapper.writeValueAsString(command);

        HttpResponse<String> response = sendRequest(url, body);
        String responseBody = response.body();

        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {});

            if (responseMap.containsKey("error")) {
                logger.error("Upstash error for GET key '{}': {}", key, responseMap.get("error"));
                return null;
            }

            Object result = responseMap.get("result");
            return (result != null) ? result.toString() : null;

        } catch (Exception e) {
            logger.error("Failed to parse Upstash response: {}", responseBody, e);
            throw new IOException("Failed to parse Upstash response", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(String key) throws IOException, InterruptedException {
        String url = upstashBaseUrl;

        List<Object> command = List.of("DEL", key);
        String body = objectMapper.writeValueAsString(command);

        sendRequest(url, body);
    }

    /**
     * Sends an HTTP POST request to Upstash.
     *
     * @param url  Endpoint URL (should be the base URL)
     * @param body JSON body as string
     * @return HttpResponse from Upstash
     */
    private HttpResponse<String> sendRequest(String url, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + upstashToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            logger.warn("Upstash request to {} with body {} returned status {}: {}",
                    url, body, response.statusCode(), response.body());
        }

        return response;
    }
}