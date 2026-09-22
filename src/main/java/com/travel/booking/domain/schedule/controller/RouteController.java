package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import com.travel.booking.domain.schedule.dto.request.RouteRequestDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<BaseResponse> createRoute(@Valid @RequestBody RouteRequestDTO request) {
        return ResponseWrapper.created("Route created successfully", routeService.createRoute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequestDTO request) {
        return ResponseWrapper.ok("Route updated successfully", routeService.updateRoute(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRoute(@PathVariable Long id) {
        return ResponseWrapper.ok("Route deleted successfully", routeService.deleteRoute(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getDetailRoute(@PathVariable Long id) {
        return ResponseWrapper.ok("Success", routeService.getDetailRoute(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getRoutes(@ModelAttribute RouteDTO keyword, Pageable pageable) {
        return ResponseWrapper.ok("Success", routeService.getRoutes(keyword, pageable));
    }
}
