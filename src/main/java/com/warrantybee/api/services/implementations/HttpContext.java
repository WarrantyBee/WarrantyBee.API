package com.warrantybee.api.services.implementations;

import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.services.interfaces.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;

/**
 * Provides HTTP context information for the current request.
 */
@Service
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HttpContext implements IHttpContext {

    private final HttpServletRequest request;
    private final ITokenService tokenService;

    @Getter
    private Long userId;

    @Getter
    private String email;

    @Getter
    private String accessToken;

    /**
     * Constructs a new {@code HttpContext} for the current HTTP request.
     *
     * @param request      the HTTP servlet request
     * @param tokenService the token validation service
     */
    @Autowired
    public HttpContext(HttpServletRequest request, ITokenService tokenService) {
        this.request = request;
        this.tokenService = tokenService;
        initialize();
    }

    /**
     * Initializes the access token and extracts claim values.
     */
    private void initialize() {
        this.accessToken = extractAccessToken();

        if (!Validator.isBlank(this.accessToken)) {
            Map<String, Object> claims = this.tokenService.validate(this.accessToken);

            this.userId = Long.parseLong(
                    claims.getOrDefault("userId", "-1").toString()
            );

            this.email = claims.getOrDefault("email", null) != null
                    ? claims.get("email").toString()
                    : null;
        }
    }

    /**
     * Extracts the Bearer token from the Authorization header.
     *
     * @return the Bearer token or {@code null} if not present
     */
    private String extractAccessToken() {
        if (request == null) return null;

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
