package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.dto.request.FleetRequestDTO;
import com.travel.booking.domain.schedule.service.FleetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fleets")
@RequiredArgsConstructor
public class FleetController {

    private final FleetService fleetService;

    @PostMapping
    public ResponseEntity<BaseResponse> createFleet(@Valid @RequestBody FleetRequestDTO request) {
        return ResponseWrapper.created("Fleet created successfully", fleetService.createFleet(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateFleet(@PathVariable Long id, @Valid @RequestBody FleetRequestDTO request) {
        return ResponseWrapper.ok("Fleet updated successfully", fleetService.updateFleet(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteFleet(@PathVariable Long id) {
        return ResponseWrapper.ok("Fleet deleted successfully", fleetService.deleteFleet(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getFleets(@ModelAttribute FleetDTO keyword, Pageable pageable) {
        return ResponseWrapper.ok("Success", fleetService.getFleets(keyword, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getFleetDetail(@PathVariable Long id) {
        return ResponseWrapper.ok("Success", fleetService.getFleetDetail(id));
    }
}
