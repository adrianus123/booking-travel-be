package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.request.ScheduleSearchRequestDTO;
import com.travel.booking.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<BaseResponse> createSchedule(@Valid @RequestBody ScheduleRequestDTO request) {
        return ResponseWrapper.created("Schedule created successfully", scheduleService.createSchedule(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDTO request) {
        return ResponseWrapper.ok("Schedule updated successfully", scheduleService.updateSchedule(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> detailSchedule(@PathVariable Long id) {
        return ResponseWrapper.ok("Success", scheduleService.getScheduleDetail(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteSchedule(@PathVariable Long id) {
        return ResponseWrapper.ok("Schedule deleted successfully", scheduleService.deleteSchedule(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getSchedules(@ModelAttribute ScheduleSearchRequestDTO keyword, Pageable pageable) {
        return ResponseWrapper.ok("Success", scheduleService.getSchedules(keyword, pageable));
    }
}
