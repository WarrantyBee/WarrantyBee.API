package com.warrantybee.api.services;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.BetterStackConfiguration;
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

    private final String host;
    private final String accessToken;
    private final HttpClient httpClient;

    /**
     * Initializes the service using the application configuration.
     *
     * @param appConfig the application configuration
     */
    public BetterStackTelemetryService(AppConfiguration appConfig) {
        BetterStackConfiguration cfg = appConfig.getBetterStackConfiguration();
        this.host = cfg.getHost();
        this.accessToken = cfg.getAccessToken();
        this.httpClient = HttpClient.newHttpClient();

        if (host == null || accessToken == null) {
            throw new ConfigurationException("Better Stack host or access token is not configured");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void trackEvent(String eventName, Map<String, Object> properties) {
        JSONObject payload = new JSONObject();
        payload.put("type", "event");
        payload.put("name", eventName);
        payload.put("properties", properties);

        sendTelemetry(payload);
    }

    /** {@inheritDoc} */
    @Override
    public void logError(Throwable throwable, Map<String, Object> context) {
        JSONObject payload = new JSONObject();
        payload.put("type", "error");
        payload.put("message", throwable.getMessage());
        payload.put("stackTrace", throwable.getStackTrace());
        payload.put("context", context);

        sendTelemetry(payload);
    }

    /** {@inheritDoc} */
    @Override
    public void trackMetric(String metricName, double value, Map<String, Object> properties) {
        JSONObject payload = new JSONObject();
        payload.put("type", "metric");
        payload.put("name", metricName);
        payload.put("value", value);
        payload.put("properties", properties);

        sendTelemetry(payload);
    }

    /** {@inheritDoc} */
    @Override
    public void flush() {
        // Placeholder: Better Stack might not require flush; implement if needed
        System.out.println("Flushing telemetry data to Better Stack");
    }

    /**
     * Sends telemetry payload to Better Stack API.
     *
     * @param payload JSON payload containing telemetry data
     */
    private void sendTelemetry(JSONObject payload) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(host))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new TelemetryServiceException("Failed to send telemetry: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new TelemetryServiceException("Failed to send telemetry data", e);
        }
    }
}