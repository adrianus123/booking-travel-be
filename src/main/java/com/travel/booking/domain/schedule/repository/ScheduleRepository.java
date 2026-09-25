package com.travel.booking.domain.schedule.repository;

import com.travel.booking.domain.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    Optional<Schedule> findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(OffsetDateTime depTime, OffsetDateTime arrTime, String fleetPlateNo, Long routeId);
}
