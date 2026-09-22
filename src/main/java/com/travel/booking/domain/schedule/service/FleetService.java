package com.travel.booking.domain.schedule.service;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.dto.request.FleetRequestDTO;
import org.springframework.data.domain.Pageable;

public interface FleetService {
    FleetDTO createFleet(FleetRequestDTO request);
    FleetDTO updateFleet(FleetRequestDTO request, Long id);
    Boolean deleteFleet(Long id);
    BasePaging<FleetDTO> getFleets(FleetDTO keyword, Pageable pageable);
    FleetDTO getFleetDetail(Long id);
}
