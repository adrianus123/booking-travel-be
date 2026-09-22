package com.travel.booking.domain.schedule.service;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.request.RouteRequestDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import org.springframework.data.domain.Pageable;

public interface RouteService {
    RouteDTO createRoute(RouteRequestDTO request);
    RouteDTO updateRoute(RouteRequestDTO request, Long id);
    Boolean deleteRoute(Long id);
    RouteDTO getDetailRoute(Long id);
    BasePaging<RouteDTO> getRoutes(RouteDTO keyword, Pageable pageable);
}
