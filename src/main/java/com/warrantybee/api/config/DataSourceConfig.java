package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.DataSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Spring configuration class responsible for creating and configuring the
 * application's primary {@link DataSource} bean using connection properties
 * retrieved from {@link AppConfiguration}.
 */
@Configuration
public class DataSourceConfig {

    private final AppConfiguration _appConfiguration;

    /**
     * Constructs the DataSourceConfig, injecting the main application configuration.
     * @param appConfiguration The overall application configuration containing data source settings.
     */
    public DataSourceConfig(AppConfiguration appConfiguration) {
        this._appConfiguration = appConfiguration;
    }

    /**
     * Creates and configures the JDBC {@link DataSource} bean.
     * <p>
     * It retrieves connection details (URL, username, password, driver class)
     * from the {@link DataSourceConfiguration} within {@link AppConfiguration}.
     * Throws an {@link IllegalStateException} if any required database configuration
     * parameter is missing.
     * </p>
     * @return The configured {@link DriverManagerDataSource} instance.
     * @throws IllegalStateException if the database configuration is incomplete.
     */
    @Bean
    public DataSource dataSource() {
        DataSourceConfiguration cfg = _appConfiguration.getDataSourceConfiguration();

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
