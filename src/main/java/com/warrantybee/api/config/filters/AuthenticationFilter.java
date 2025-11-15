package com.warrantybee.api.config.filters;

import com.warrantybee.api.services.implementations.JwtTokenService;
import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.exceptions.SessionExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public AuthenticationFilter(JwtTokenService tokenService) {
        this._tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
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
            response.getWriter().write("Invalid or expired JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
