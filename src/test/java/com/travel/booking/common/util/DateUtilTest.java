package com.travel.booking.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    @DisplayName("getEarliestTime - Mengembalikan awal hari dalam UTC")
    void getEarliestTime_ReturnsStartOfDayInUtc() {
        LocalDate date = LocalDate.of(2026, Month.SEPTEMBER, 25);

        OffsetDateTime result = DateUtil.getEarliestTime(date);

        assertThat(result).isEqualTo(OffsetDateTime.parse("2026-09-25T00:00Z"));
        assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
    }

    @Test
    @DisplayName("getLatestTime - Mengembalikan akhir hari dalam UTC")
    void getLatestTime_ReturnsEndOfDayInUtc() {
        LocalDate date = LocalDate.of(2026, Month.SEPTEMBER, 25);

        OffsetDateTime result = DateUtil.getLatestTime(date);

        assertThat(result).isEqualTo(OffsetDateTime.parse("2026-09-25T23:59:59.999999999Z"));
        assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
    }
}
