package com.warrantybee.api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.Error;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet filter for applying rate limiting to specific API endpoints
 * using the Bucket4j library.
 */
@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if ("/api/auth/send-otp".equals(path)) {
            String clientId = getClientIdentifier(request);
            Bucket bucket = cache.computeIfAbsent(clientId, this::createNewBucket);

            if (!bucket.tryConsume(1)) {
                APIError apiError = new APIError(Error.RATE_LIMIT_EXCEEDED.getCode(), Error.RATE_LIMIT_EXCEEDED.getMessage());
                APIResponse<?> apiResponse = new APIResponse<>(apiError);

                response.setStatus(Error.RATE_LIMIT_EXCEEDED.getStatus().value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                return;
            }
        }

        chain.doFilter(req, res);
    }

    /**
     * Returns a unique identifier for the client, typically the IP address.
     */
    private String getClientIdentifier(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * Creates a new rate-limiting bucket for a client.
     * Allows 1 request every 2 minutes.
     */
    private Bucket createNewBucket(String clientId) {
        Refill refill = Refill.intervally(1, Duration.ofMinutes(30));
        Bandwidth limit = Bandwidth.classic(1, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}
