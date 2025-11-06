package com.warrantybee.api.config;

import com.warrantybee.api.filters.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for registering the {@link RateLimitFilter} in the Spring Boot application.
 */
@Configuration
public class RateLimitFilterConfig {

    /**
     * Registers the {@link RateLimitFilter} as a servlet filter for the specified URL pattern.
     *
     * @param filter the rate-limiting filter bean to be registered
     * @return a configured {@link FilterRegistrationBean} that registers the filter
     */
    @Bean
    public FilterRegistrationBean<RateLimitFilter> register(RateLimitFilter filter) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/auth/send-otp");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
