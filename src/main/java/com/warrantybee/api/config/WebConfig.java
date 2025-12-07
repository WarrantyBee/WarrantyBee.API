package com.warrantybee.api.config;

import com.warrantybee.api.config.filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * Web configuration class that registers application-level interceptors.
 * Supports excluding whitelisted endpoints from security filtering.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SecurityFilter _filter;

    @Value("${APP_WHITELISTED_ENDPOINTS:}")
    private String _WHITELISTED_ENDPOINTS;

    @Autowired
    WebConfig(SecurityFilter filter) {
        this._filter = filter;
    }

    /**
     * Registers the {@link SecurityFilter} and applies it to all paths,
     * excluding whitelisted endpoints specified in the application properties.
     *
     * @param registry the interceptor registry used to register interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludedPaths = Arrays.stream(_WHITELISTED_ENDPOINTS.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        registry.addInterceptor(_filter)
                .addPathPatterns("/**")
                .excludePathPatterns(excludedPaths);
    }
}
