package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for defining the properties needed to establish a
 * database connection (data source).
 */
@Getter
@Setter
public class DataSourceConfiguration {

    /** The JDBC connection URL for the database. */
    private String url;

    /** The username required to authenticate with the database. */
    private String username;

    /** The password required to authenticate with the database. */
    private String password;

    /** The fully qualified class name of the JDBC driver. */
    private String driver;
}
