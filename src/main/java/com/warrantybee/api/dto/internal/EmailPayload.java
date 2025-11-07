package com.warrantybee.api.dto.internal;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the payload for sending an email.
 */
@Getter
@Setter
public class EmailPayload {
    /** Recipient email addresses */
    private String[] to;

    /** CC email addresses */
    private String[] cc;

    /** BCC email addresses */
    private String[] bcc;

    /** Email subject */
    private String subject;

    /** Email body content */
    private String body;

    /** File attachments */
    private String[] attachments;
}
