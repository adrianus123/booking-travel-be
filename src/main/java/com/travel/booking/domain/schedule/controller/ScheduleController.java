package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<BaseResponse> createSchedule(@Valid @RequestBody ScheduleRequestDTO request) {
        return ResponseWrapper.created("Schedule created successfully", scheduleService.createSchedule(request));
    }
}
