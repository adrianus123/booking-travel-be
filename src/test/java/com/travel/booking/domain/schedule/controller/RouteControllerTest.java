package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.domain.schedule.dto.request.RouteRequestDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.service.RouteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    @Test
    @DisplayName("createRoute - Berhasil membuat rute dan meneruskan request")
    void createRoute_Success() {
        RouteRequestDTO request = new RouteRequestDTO();
        request.setDepCity("Jakarta");
        request.setDestCity("Bandung");
        request.setPrice(new BigDecimal("150000"));
        RouteDTO data = RouteDTO.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();
        when(routeService.createRoute(request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = routeController.createRoute(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Route created successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(routeService, times(1)).createRoute(request);
    }

    @Test
    @DisplayName("updateRoute - Berhasil memperbarui rute dan meneruskan request")
    void updateRoute_Success() {
        RouteRequestDTO request = new RouteRequestDTO();
        request.setDepCity("Jakarta");
        request.setDestCity("Bandung");
        request.setPrice(new BigDecimal("150000"));
        RouteDTO data = RouteDTO.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();
        when(routeService.updateRoute(request, 1L)).thenReturn(data);

        ResponseEntity<BaseResponse> response = routeController.updateRoute(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Route updated successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(routeService, times(1)).updateRoute(request, 1L);
    }

    @Test
    @DisplayName("deleteRoute - Berhasil menghapus rute dan meneruskan id")
    void deleteRoute_Success() {
        when(routeService.deleteRoute(1L)).thenReturn(true);

        ResponseEntity<BaseResponse> response = routeController.deleteRoute(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Route deleted successfully");
        assertThat(body.getData()).isEqualTo(true);
        assertThat(body.getErrors()).isNull();
        verify(routeService, times(1)).deleteRoute(1L);
    }

    @Test
    @DisplayName("getDetailRoute - Berhasil mengembalikan detail rute")
    void getDetailRoute_Success() {
        RouteDTO data = RouteDTO.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();
        when(routeService.getDetailRoute(1L)).thenReturn(data);

        ResponseEntity<BaseResponse> response = routeController.getDetailRoute(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(routeService, times(1)).getDetailRoute(1L);
    }

    @Test
    @DisplayName("getRoutes - Berhasil mengembalikan data berpaginasi")
    void getRoutes_Success() {
        RouteDTO keyword = RouteDTO.builder()
                .depCity("Jakarta")
                .destCity("Bandung")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        RouteDTO route = RouteDTO.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();
        BasePaging<RouteDTO> data = BasePaging.<RouteDTO>builder()
                .data(List.of(route))
                .page(0)
                .size(10)
                .totalPage(1)
                .totalData(1L)
                .build();
        when(routeService.getRoutes(keyword, pageable)).thenReturn(data);

        ResponseEntity<BaseResponse> response = routeController.getRoutes(keyword, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(routeService, times(1)).getRoutes(keyword, pageable);
    }
}
