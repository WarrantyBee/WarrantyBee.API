package com.warrantybee.api.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.Error;
import com.warrantybee.api.services.implementations.JwtTokenService;
import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.exceptions.SessionExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService _tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthenticationFilter(JwtTokenService tokenService) {
        this._tokenService = tokenService;
    }

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/status",
            "/status.html",
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/forgotpassword",
            "/api/auth/resetpassword",
            "/api/countries"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (PUBLIC_URLS.stream().anyMatch(url -> request.getRequestURI().equals(url))) {
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

        String token = header.substring(7);

        try {
            Map<String, Object> claims = _tokenService.validate(token);

            String userId = claims.get("userId").toString();
            String email = claims.get("email").toString();

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            var authToken = new UsernamePasswordAuthenticationToken(
                    email,
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
