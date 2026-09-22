package com.travel.booking.domain.schedule.repository;

import com.travel.booking.domain.schedule.model.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, Long>, JpaSpecificationExecutor<Fleet> {
    Optional<Fleet> findByPlateNo(String plateNo);
}
