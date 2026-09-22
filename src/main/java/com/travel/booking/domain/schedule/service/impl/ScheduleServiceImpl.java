package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.dto.response.ScheduleDTO;
import com.travel.booking.domain.schedule.model.Fleet;
import com.travel.booking.domain.schedule.model.Route;
import com.travel.booking.domain.schedule.model.Schedule;
import com.travel.booking.domain.schedule.repository.FleetRepository;
import com.travel.booking.domain.schedule.repository.RouteRepository;
import com.travel.booking.domain.schedule.repository.ScheduleRepository;
import com.travel.booking.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.travel.booking.common.util.StringUtil.formattingPrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final FleetRepository fleetRepository;
    private final RouteRepository routeRepository;

    @Override
    @Transactional
    public ScheduleDTO createSchedule(ScheduleRequestDTO request) {
        try {
            Fleet fleet = getScheduleFleet(request.getPlateNo());
            Route route = getScheduleRoute(request.getRouteId());

            boolean isDuplicate = checkDuplicateSchedule(request);
            if (isDuplicate) {
                throw new IllegalArgumentException("Schedule already exists");
            }

            Schedule schedule = Schedule.builder()
                    .fleet(fleet)
                    .route(route)
                    .depTime(request.getDepTime())
                    .arrTime(request.getArrTime())
                    .build();

            return constructScheduleDto(scheduleRepository.save(schedule));
        } catch (Exception ex) {
            log.error("Failed to create schedule: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ScheduleDTO updateSchedule(Long id, ScheduleRequestDTO request) {
        try {
            Schedule schedule = getScheduleById(id);
            Fleet fleet = getScheduleFleet(request.getPlateNo());
            Route route = getScheduleRoute(request.getRouteId());

            if (!schedule.getDepTime().isEqual(request.getDepTime())
                    || !schedule.getArrTime().isEqual(request.getArrTime())) {
                boolean isDuplicate = checkDuplicateSchedule(request);
                if (isDuplicate) {
                    throw new IllegalArgumentException("Schedule already exists");
                }
            }

            schedule.setFleet(fleet);
            schedule.setRoute(route);
            schedule.setDepTime(request.getDepTime());
            schedule.setArrTime(request.getArrTime());

            return constructScheduleDto(scheduleRepository.save(schedule));
        } catch (Exception ex) {
            log.error("Failed to update schedule: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ScheduleDTO getScheduleDetail(Long id) {
        Schedule schedule = getScheduleById(id);
        return constructScheduleDto(schedule);
    }

    private Fleet getScheduleFleet(String plateNo) {
        return fleetRepository.findByPlateNo(plateNo)
                .orElseThrow(() -> new IllegalArgumentException("Fleet not found"));
    }

    private Route getScheduleRoute(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
    }

    private Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    }

    private boolean checkDuplicateSchedule(ScheduleRequestDTO request) {
        return scheduleRepository.findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                request.getDepTime(),
                request.getArrTime(),
                request.getPlateNo(),
                request.getRouteId()
        ).isPresent();
    }

    private ScheduleDTO constructScheduleDto(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .fleet(constructFleetDto(schedule.getFleet()))
                .route(constructRouteDTO(schedule.getRoute()))
                .depTime(schedule.getDepTime())
                .arrTime(schedule.getArrTime())
                .build();
    }

    private RouteDTO constructRouteDTO(Route entity) {
        return RouteDTO.builder()
                .id(entity.getId())
                .depCity(entity.getDepCity())
                .destCity(entity.getDestCity())
                .price(formattingPrice(entity.getPrice()))
                .build();
    }

    private FleetDTO constructFleetDto(Fleet entity) {
        return FleetDTO.builder()
                .id(entity.getId())
                .plateNo(entity.getPlateNo())
                .model(entity.getModel())
                .tSeats(entity.getTSeats())
                .build();
    }
}
