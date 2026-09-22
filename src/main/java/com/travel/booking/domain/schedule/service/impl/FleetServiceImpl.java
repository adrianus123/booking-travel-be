package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.dto.request.FleetRequestDTO;
import com.travel.booking.domain.schedule.model.Fleet;
import com.travel.booking.domain.schedule.repository.FleetRepository;
import com.travel.booking.domain.schedule.service.FleetService;
import com.travel.booking.domain.schedule.service.specification.FleetSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FleetServiceImpl implements FleetService {

    private final FleetRepository fleetRepository;

    @Override
    @Transactional
    public FleetDTO createFleet(FleetRequestDTO request) {
        try {
            boolean isPlateNoExists = fleetRepository.findByPlateNo(request.getPlateNo()).isPresent();
            if (isPlateNoExists) {
                throw new IllegalArgumentException("Plate number of this fleet is exists");
            }

            Fleet fleet = Fleet.builder()
                    .plateNo(request.getPlateNo())
                    .model(request.getModel())
                    .tSeats(request.getTSeats())
                    .build();

            return constructFleetDto(fleetRepository.save(fleet));
        } catch (Exception ex) {
            log.error("Failed create fleet: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public FleetDTO updateFleet(FleetRequestDTO request, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Fleet id cannot be null");
        }

        Fleet fleet = checkFleetExists(id);
        if (validateDuplicatePlateNumber(fleet, request)) {
            throw new IllegalArgumentException("Plate number of this fleet is exists");
        }

        fleet.setPlateNo(request.getPlateNo());
        fleet.setModel(request.getModel());
        fleet.setTSeats(request.getTSeats());

        return constructFleetDto(fleetRepository.save(fleet));
    }

    @Override
    @Transactional
    public Boolean deleteFleet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Fleet id cannot be null");
        }

        Fleet fleet = checkFleetExists(id);
        fleetRepository.delete(fleet);

        return !fleetRepository.existsById(id);
    }

    @Override
    public BasePaging<FleetDTO> getFleets(FleetDTO keyword, Pageable pageable) {
        Specification<Fleet> specification = Specification.where(FleetSpecification.plateNoContains(keyword.getPlateNo()))
                .and(FleetSpecification.modelContains(keyword.getModel()))
                .and(FleetSpecification.hasTotalSeat(keyword.getTSeats()));

        Page<Fleet> fleets = fleetRepository.findAll(specification, pageable);
        List<FleetDTO> fleetDTOList = fleets.stream().map(this::constructFleetDto).toList();

        return BasePaging.<FleetDTO>builder()
                .data(fleetDTOList)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(fleets.getTotalPages())
                .totalData(fleets.getTotalElements())
                .build();
    }

    @Override
    public FleetDTO getFleetDetail(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Fleet id cannot be null");
        }

        Fleet fleet = checkFleetExists(id);
        return constructFleetDto(fleet);
    }

    private Fleet checkFleetExists(Long id) {
        Optional<Fleet> optFleet = fleetRepository.findById(id);

        if (optFleet.isEmpty()) {
            log.error("Fleet with id {} not exists", id);
            throw new IllegalArgumentException("Fleet not found");
        }

        return optFleet.get();
    }

    private boolean validateDuplicatePlateNumber(Fleet entity, FleetRequestDTO dto) {
        if (entity.getPlateNo().equalsIgnoreCase(dto.getPlateNo())) {
            return false;
        }

        Optional<Fleet> fleet = fleetRepository.findByPlateNo(dto.getPlateNo());
        return fleet.isPresent();
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
