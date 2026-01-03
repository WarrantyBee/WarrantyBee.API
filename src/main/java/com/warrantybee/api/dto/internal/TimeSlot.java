package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents a discrete time interval with a defined start and end timestamp.
 * Both {@code start} and {@code end} timestamps are expected to be in UTC
 * unless explicitly documented otherwise by the consuming service.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {

    /** The starting timestamp of the time slot. */
    private Timestamp start;

    /** The ending timestamp of the time slot. */
    private Timestamp end;
}
