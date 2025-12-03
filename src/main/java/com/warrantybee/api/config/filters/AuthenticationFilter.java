package com.warrantybee.api.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.Error;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;
import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.exceptions.SessionExpiredException;
import com.warrantybee.api.services.interfaces.IHttpContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final IHttpContext _httpContext;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthenticationFilter(IHttpContext httpContext) {
        this._httpContext = httpContext;
    }

    @Value("${APP_WHITELISTED_ENDPOINTS:}")
    private String _WHITELISTED_ENDPOINTS;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final List<String> whitelistedEndpoints = Arrays.stream(_WHITELISTED_ENDPOINTS.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty()).toList();

        if (whitelistedEndpoints.stream().anyMatch(url -> request.getRequestURI().equals(url))) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                objectMapper.writeValueAsString(
                    new APIResponse<>(null,
                        new APIError(
                            Error.INVALID_AUTH_HEADER.getCode(),
                            Error.INVALID_AUTH_HEADER.getMessage()
                        )
                    )
                )
            );
            return;
        }


        try {
            _httpContext.initialize();
            Long userId = _httpContext.getUserId();
            Object principal = _httpContext.getEmail();
            SecurityRole userRole = _httpContext.getRole();
            List<SecurityPermission> userPermissions = _httpContext.getPermissions();

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));

            for (SecurityPermission permission : userPermissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }

            var authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );

            authToken.setDetails(userId);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (SessionExpiredException | InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                objectMapper.writeValueAsString(
                    new APIResponse<>(null,
                        new APIError(
                            Error.INVALID_EXPIRED_TOKEN.getCode(),
                            Error.INVALID_EXPIRED_TOKEN.getMessage()
                        )
                    )
                )
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
