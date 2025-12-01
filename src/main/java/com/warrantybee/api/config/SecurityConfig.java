package com.warrantybee.api.config;

import com.warrantybee.api.config.filters.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configures application security, including authentication, CORS settings,
 * session policy, and whitelisted endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${APP_WHITELISTED_ENDPOINTS:}")
    private String _WHITELISTED_ENDPOINTS;

    @Value("${APP_ALLOWED_ORIGINS:}")
    private String _ALLOWED_ORIGINS;

    private final AuthenticationFilter _filter;

    /**
     * Creates the security configuration with the authentication filter.
     *
     * @param filter the custom authentication filter
     */
    @Autowired
    public SecurityConfig(AuthenticationFilter filter) {
        this._filter = filter;
    }

    /**
     * Defines the security filter chain, configuring CORS, CSRF, authentication rules,
     * and stateless session management.
     *
     * @param http the HTTP security builder
     * @return the configured security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {

        http.cors(withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(_WHITELISTED_ENDPOINTS.split(",")).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(_filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS based on allowed origins defined in application properties.
     *
     * @return the CORS configuration source
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(_ALLOWED_ORIGINS.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
