package com.travel.booking.domain.schedule.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FleetRequestDTO {
    @NotEmpty(message = "Fleet plate number cannot be empty")
    @Size(max = 20)
    private String plateNo;

    @NotEmpty(message = "Fleet model cannot be empty")
    @Size(max = 100)
    private String model;

    @NotNull(message = "Fleet total seats cannot be empty")
    @Min(1)
    private Integer tSeats;
}
