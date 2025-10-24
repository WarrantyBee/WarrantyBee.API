package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for database data source connection.
 */
@Getter
@Setter
public class DataSourceConfiguration {

    /** Database connection URL */
    private String url;

    /** Database username */
    private String username;

    /** Database password */
    private String password;

    /** Database driver class name */
    private String driver;
}
