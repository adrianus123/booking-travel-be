package com.travel.booking.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateUtil {

    private DateUtil() {
        /* This utility class should not be instantiated */
    }

    public static OffsetDateTime getEarliestTime(LocalDate date) {
        return date.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime getLatestTime(LocalDate date) {
        return date.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
    }
}
