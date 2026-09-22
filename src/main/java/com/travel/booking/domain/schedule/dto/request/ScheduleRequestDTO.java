package com.travel.booking.domain.schedule.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ScheduleRequestDTO {
    @NotEmpty(message = "Fleet cannot be empty")
    private String plateNo;

    @NotNull(message = "Route cannot be empty")
    private Long routeId;

    @NotNull(message = "Departure time cannot be empty")
    private OffsetDateTime depTime;

    @NotNull(message = "Arrival time cannot be empty")
    private OffsetDateTime arrTime;
}
