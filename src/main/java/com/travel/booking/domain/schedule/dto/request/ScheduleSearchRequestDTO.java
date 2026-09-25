package com.travel.booking.domain.schedule.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleSearchRequestDTO {
    private String plateNo;
    private String model;
    private String depCity;
    private String destCity;
    private LocalDate depDate;
    private LocalDate arrDate;
}
