package com.travel.booking.domain.schedule.service;

import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.response.ScheduleDTO;

public interface ScheduleService {
    ScheduleDTO createSchedule(ScheduleRequestDTO request);
}
