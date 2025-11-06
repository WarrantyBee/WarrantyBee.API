package com.warrantybee.api.filters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
                response.setStatus(429);
                response.getWriter().write("Rate limit exceeded. Try again after 2 minutes.");
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
        Refill refill = Refill.intervally(1, Duration.ofMinutes(2)); // 1 token every 2 minutes
        Bandwidth limit = Bandwidth.classic(1, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}
