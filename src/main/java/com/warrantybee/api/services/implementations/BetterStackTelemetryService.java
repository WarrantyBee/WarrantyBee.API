package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.BetterStackConfiguration;
import com.warrantybee.api.enumerations.LogLevel;
import com.warrantybee.api.exceptions.ConfigurationException;
import com.warrantybee.api.exceptions.TelemetryServiceException;
import com.warrantybee.api.services.interfaces.ITelemetryService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Telemetry service implementation using Better Stack.
 * Reads URL and access token from {@link BetterStackConfiguration}.
 */
@Service
public class BetterStackTelemetryService implements ITelemetryService {

    private final String _host;
    private final String _accessToken;
    private final HttpClient _httpClient;

    /**
     * Initializes the service using the application configuration.
     *
     * @param appConfig the application configuration
     */
    public BetterStackTelemetryService(AppConfiguration appConfig) {
        BetterStackConfiguration cfg = appConfig.getBetterStackConfiguration();
        this._host = cfg.getHost();
        this._accessToken = cfg.getAccessToken();
        this._httpClient = HttpClient.newHttpClient();

        if (_host == null || _accessToken == null) {
            throw new ConfigurationException("Better Stack host or access token is not configured");
        }
    }

    @Override
    public void trackEvent(String eventName, Map<String, Object> properties) {
        JSONObject payload = new JSONObject();
        payload.put("type", "event");
        payload.put("name", eventName);
        payload.put("properties", properties);

        _send(payload);
    }

    @Override
    public void log(LogLevel level, String message, Map<String, Object> context) {
        JSONObject payload = new JSONObject();
        payload.put("level", level.toString());
        payload.put("message", message);
        payload.put("context", context);

        _send(payload);
    }

    @Override
    public void log(LogLevel level, Throwable throwable, Map<String, Object> context) {
        JSONObject payload = new JSONObject();
        payload.put("level", level.toString());
        payload.put("message", throwable.getMessage());
        payload.put("stackTrace", throwable.getStackTrace());
        payload.put("context", context);

        _send(payload);
    }

    @Override
    public void trackMetric(String metricName, double value, Map<String, Object> properties) {
        JSONObject payload = new JSONObject();
        payload.put("type", "metric");
        payload.put("name", metricName);
        payload.put("value", value);
        payload.put("properties", properties);

        _send(payload);
    }

    @Override
    public void flush() {
        // The HTTP client sends requests synchronously, so there's no buffer to flush.
        // This method is implemented to satisfy the interface contract.
    }

    private void _send(JSONObject payload) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(_host))
                .header("Authorization", "Bearer " + _accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        try {
            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new TelemetryServiceException("Failed to send telemetry: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new TelemetryServiceException("Failed to send telemetry data", e);
        }
    }
}