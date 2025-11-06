package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents SMTP server configuration details required for sending emails.
 */
@Getter
@Setter
public class SmtpConfiguration {

    /** The SMTP server host address. */
    private String host;

    /** The SMTP server port number. */
    private Integer port;

    /** The username used for SMTP authentication. */
    private String username;

    /** The password used for SMTP authentication. */
    private String password;
}
