package com.travel.booking.domain.schedule.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RouteRequestDTO {
    @NotEmpty(message = "Departure City cannot be empty")
    @Size(max = 100)
    private String depCity;

    @NotEmpty(message = "Destination City cannot be empty")
    @Size(max = 100)
    private String destCity;

    @NotNull(message = "Price cannot be empty")
    private BigDecimal price;
}
