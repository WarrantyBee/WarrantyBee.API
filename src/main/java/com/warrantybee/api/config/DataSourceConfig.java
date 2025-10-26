package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.DataSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Configures the application's DataSource using AppConfiguration.
 */
@Configuration
public class DataSourceConfig {

    private final AppConfiguration appConfiguration;

    public DataSourceConfig(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    /**
     * Creates a DataSource bean from DataSourceConfiguration.
     *
     * @return configured DataSource
     */
    @Bean
    public DataSource dataSource() {
        DataSourceConfiguration cfg = appConfiguration.getDataSourceConfiguration();

        if (cfg.getUrl() == null || cfg.getUsername() == null || cfg.getPassword() == null || cfg.getDriver() == null) {
            throw new IllegalStateException("Incomplete database configuration");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(cfg.getDriver());
        dataSource.setUrl(cfg.getUrl());
        dataSource.setUsername(cfg.getUsername());
        dataSource.setPassword(cfg.getPassword());

        return dataSource;
    }
}
