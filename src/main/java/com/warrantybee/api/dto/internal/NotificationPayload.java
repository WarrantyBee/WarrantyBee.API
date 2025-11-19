package com.warrantybee.api.dto.internal;

import com.warrantybee.api.enumerations.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Represents the payload data for sending notifications,
 * including the recipient and dynamic macro values to personalize the message.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {

    /** The recipient's email address or identifier. */
    private String recipient;

    /** Key-value pairs of dynamic macros used for message personalization. */
    private Map<String, String> dynamicMacros;

    /** The type of notification to send the user. */
    private NotificationType type;
}
