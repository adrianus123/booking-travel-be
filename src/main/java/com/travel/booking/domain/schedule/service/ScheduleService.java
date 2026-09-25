package com.travel.booking.domain.schedule.service;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.request.ScheduleSearchRequestDTO;
import com.travel.booking.domain.schedule.dto.response.ScheduleDTO;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    ScheduleDTO createSchedule(ScheduleRequestDTO request);
    ScheduleDTO updateSchedule(Long id, ScheduleRequestDTO request);
    ScheduleDTO getScheduleDetail(Long id);
    boolean deleteSchedule(Long id);
    BasePaging<ScheduleDTO> getSchedules(ScheduleSearchRequestDTO keyword, Pageable pageable);
}
