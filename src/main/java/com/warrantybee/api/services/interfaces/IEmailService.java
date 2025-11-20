package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.NotificationPayload;

/**
 * Defines the contract for email-related operations such as sending
 * standard emails and generic notifications.
 */
public interface IEmailService {

    /**
     * Sends an email based on the provided notification payload by generating
     * the message content, applying macros, and delivering it through the mail sender.
     *
     * @param notification the notification payload containing recipient and dynamic data
     */
    void send(NotificationPayload notification);
}
