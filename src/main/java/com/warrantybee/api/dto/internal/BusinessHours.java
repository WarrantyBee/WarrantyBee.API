package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents weekly business operating hours, organized by day of the week.
 * <p>
 * Each day contains a list of {@link TimeSlot} entries defining active or
 * available time intervals for that day.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessHours {

    /** Business hours for Monday. */
    private List<TimeSlot> monday;

    /** Business hours for Tuesday. */
    private List<TimeSlot> tuesday;

    /** Business hours for Wednesday. */
    private List<TimeSlot> wednesday;

    /** Business hours for Thursday. */
    private List<TimeSlot> thursday;

    /** Business hours for Friday. */
    private List<TimeSlot> friday;

    /** Business hours for Saturday. */
    private List<TimeSlot> saturday;

    /** Business hours for Sunday. */
    private List<TimeSlot> sunday;
}
