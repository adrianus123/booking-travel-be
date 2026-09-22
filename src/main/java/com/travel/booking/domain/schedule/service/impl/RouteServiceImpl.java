package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.request.RouteRequestDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.model.Route;
import com.travel.booking.domain.schedule.repository.RouteRepository;
import com.travel.booking.domain.schedule.service.RouteService;
import com.travel.booking.domain.schedule.service.specification.RouteSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.travel.booking.common.util.StringUtil.formattingPrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    @Transactional
    public RouteDTO createRoute(RouteRequestDTO request) {
        Route route = Route.builder()
                .depCity(request.getDepCity())
                .destCity(request.getDestCity())
                .price(request.getPrice())
                .build();

        return constructRouteDTO(routeRepository.save(route));
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(RouteRequestDTO request, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Route id cannot be null");
        }

        Route route = checkEntityExists(id);
        route.setDepCity(request.getDepCity());
        route.setDestCity(request.getDestCity());
        route.setPrice(request.getPrice());

        return constructRouteDTO(routeRepository.save(route));
    }

    @Override
    @Transactional
    public Boolean deleteRoute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Route id cannot be null");
        }

        Route route = checkEntityExists(id);
        routeRepository.delete(route);

        return !routeRepository.existsById(id);
    }

    @Override
    public RouteDTO getDetailRoute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Route id cannot be null");
        }

        Route route = checkEntityExists(id);

        return constructRouteDTO(route);
    }

    @Override
    public BasePaging<RouteDTO> getRoutes(RouteDTO keyword, Pageable pageable) {
        Specification<Route> specification = Specification
                .where(RouteSpecification.depCityContains(keyword.getDepCity()))
                .and(RouteSpecification.destCityContains(keyword.getDestCity()))
                .and(RouteSpecification.hasPrice(keyword.getPrice()));

        Page<Route> routes = routeRepository.findAll(specification, pageable);
        List<RouteDTO> dtoList = routes.stream().map(this::constructRouteDTO).toList();

        return BasePaging.<RouteDTO>builder()
                .data(dtoList)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(routes.getTotalPages())
                .totalData(routes.getTotalElements())
                .build();
    }

    private Route checkEntityExists(Long id) {
        Optional<Route> optRoute = routeRepository.findById(id);

        if (optRoute.isEmpty()) {
            log.error("Route with id {} not found", id);
            throw new IllegalArgumentException("Route not found");
        }

        return optRoute.get();
    }

    private RouteDTO constructRouteDTO(Route entity) {
        return RouteDTO.builder()
                .id(entity.getId())
                .depCity(entity.getDepCity())
                .destCity(entity.getDestCity())
                .price(formattingPrice(entity.getPrice()))
                .build();
    }
}
