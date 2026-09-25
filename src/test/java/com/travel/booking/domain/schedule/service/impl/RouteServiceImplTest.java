package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.request.RouteRequestDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.model.Route;
import com.travel.booking.domain.schedule.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.travel.booking.common.util.StringUtil.formattingPrice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteServiceImpl routeService;

    private RouteRequestDTO requestDummy;
    private Route routeDummy;

    @BeforeEach
    void setUp() {
        requestDummy = new RouteRequestDTO();
        requestDummy.setDepCity("Jakarta");
        requestDummy.setDestCity("Bandung");
        requestDummy.setPrice(new BigDecimal("150000"));

        routeDummy = Route.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price(new BigDecimal("150000"))
                .build();
    }

    @Test
    @DisplayName("createRoute - Berhasil menyimpan data rute")
    void createRoute_Success() {
        when(routeRepository.save(any(Route.class))).thenReturn(routeDummy);

        RouteDTO result = routeService.createRoute(requestDummy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepCity()).isEqualTo("Jakarta");
        assertThat(result.getDestCity()).isEqualTo("Bandung");
        assertThat(result.getPrice()).isEqualTo(formattingPrice(new BigDecimal("150000")));

        ArgumentCaptor<Route> routeCaptor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository, times(1)).save(routeCaptor.capture());
        assertThat(routeCaptor.getValue().getDepCity()).isEqualTo(requestDummy.getDepCity());
        assertThat(routeCaptor.getValue().getDestCity()).isEqualTo(requestDummy.getDestCity());
        assertThat(routeCaptor.getValue().getPrice()).isEqualTo(requestDummy.getPrice());
    }

    @Test
    @DisplayName("updateRoute - Berhasil memperbarui data rute")
    void updateRoute_Success() {
        RouteRequestDTO updateRequestDummy = new RouteRequestDTO();
        updateRequestDummy.setDepCity("Surabaya");
        updateRequestDummy.setDestCity("Malang");
        updateRequestDummy.setPrice(new BigDecimal("200000"));

        Route routeAfterSaveDummy = Route.builder()
                .id(1L)
                .depCity("Surabaya")
                .destCity("Malang")
                .price(new BigDecimal("200000"))
                .build();

        when(routeRepository.findById(1L)).thenReturn(Optional.of(routeDummy));
        when(routeRepository.save(any(Route.class))).thenReturn(routeAfterSaveDummy);

        RouteDTO result = routeService.updateRoute(updateRequestDummy, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepCity()).isEqualTo("Surabaya");
        assertThat(result.getDestCity()).isEqualTo("Malang");
        assertThat(result.getPrice()).isEqualTo(formattingPrice(new BigDecimal("200000")));

        ArgumentCaptor<Route> routeCaptor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).save(routeCaptor.capture());
        assertThat(routeCaptor.getValue().getDepCity()).isEqualTo(updateRequestDummy.getDepCity());
        assertThat(routeCaptor.getValue().getDestCity()).isEqualTo(updateRequestDummy.getDestCity());
        assertThat(routeCaptor.getValue().getPrice()).isEqualTo(updateRequestDummy.getPrice());
    }

    @Test
    @DisplayName("updateRoute - Gagal & melempar Exception ID tidak boleh kosong")
    void updateRoute_Failed_IdIsNull() {
        assertThatThrownBy(() -> routeService.updateRoute(requestDummy, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route id cannot be null");

        verify(routeRepository, never()).findById(any(Long.class));
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    @DisplayName("updateRoute - Gagal & melempar Exception data tidak ditemukan")
    void updateRoute_Failed_EntityNotFound() {
        when(routeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.updateRoute(requestDummy, 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route not found");

        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    @DisplayName("deleteRoute - Berhasil menghapus data")
    void deleteRoute_Success() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(routeDummy));
        when(routeRepository.existsById(1L)).thenReturn(false);

        Boolean result = routeService.deleteRoute(1L);

        assertThat(result.booleanValue()).isTrue();

        verify(routeRepository, times(1)).delete(any(Route.class));
    }

    @Test
    @DisplayName("deleteRoute - Mengembalikan false ketika data masih tersedia")
    void deleteRoute_Success_EntityStillExists() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(routeDummy));
        when(routeRepository.existsById(1L)).thenReturn(true);

        Boolean result = routeService.deleteRoute(1L);

        assertThat(result.booleanValue()).isFalse();

        verify(routeRepository, times(1)).delete(any(Route.class));
    }

    @Test
    @DisplayName("deleteRoute - Gagal & melempar Exception ID tidak boleh kosong")
    void deleteRoute_Failed_IdIsNull() {
        assertThatThrownBy(() -> routeService.deleteRoute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route id cannot be null");

        verify(routeRepository, never()).findById(any(Long.class));
        verify(routeRepository, never()).delete(any(Route.class));
        verify(routeRepository, never()).existsById(any(Long.class));
    }

    @Test
    @DisplayName("deleteRoute - Gagal & melempar Exception data tidak ditemukan")
    void deleteRoute_Failed_EntityNotFound() {
        when(routeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.deleteRoute(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route not found");

        verify(routeRepository, never()).delete(any(Route.class));
        verify(routeRepository, never()).existsById(any(Long.class));
    }

    @Test
    @DisplayName("getDetailRoute - Berhasil menampilkan detail rute")
    void getDetailRoute_Success() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(routeDummy));

        RouteDTO result = routeService.getDetailRoute(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepCity()).isEqualTo("Jakarta");
        assertThat(result.getDestCity()).isEqualTo("Bandung");
        assertThat(result.getPrice()).isEqualTo(formattingPrice(new BigDecimal("150000")));
    }

    @Test
    @DisplayName("getDetailRoute - Gagal & melempar Exception ID tidak boleh kosong")
    void getDetailRoute_Failed_IdIsNull() {
        assertThatThrownBy(() -> routeService.getDetailRoute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route id cannot be null");

        verify(routeRepository, never()).findById(any(Long.class));
    }

    @Test
    @DisplayName("getDetailRoute - Gagal & melempar Exception data tidak ditemukan")
    void getDetailRoute_Failed_EntityNotFound() {
        when(routeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.getDetailRoute(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route not found");
    }

    @Test
    @DisplayName("getRoutes - Berhasil mengembalikan data berpaginasi saat data ditemukan")
    void getRoutes_Success_DataExists() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        RouteDTO keywordDummy = RouteDTO.builder()
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();

        List<Route> routeList = List.of(routeDummy);
        Page<Route> routePage = new PageImpl<>(routeList, pageableDummy, 1);

        when(routeRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(routePage);

        BasePaging<RouteDTO> result = routeService.getRoutes(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPage()).isEqualTo(1);
        assertThat(result.getTotalData()).isEqualTo(1L);

        RouteDTO actualDto = result.getData().getFirst();
        assertThat(actualDto.getId()).isEqualTo(1L);
        assertThat(actualDto.getDepCity()).isEqualTo("Jakarta");
        assertThat(actualDto.getDestCity()).isEqualTo("Bandung");
        assertThat(actualDto.getPrice()).isEqualTo(formattingPrice(new BigDecimal("150000")));

        verify(routeRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }

    @Test
    @DisplayName("getRoutes - Berhasil mengembalikan halaman kosong saat data tidak ditemukan")
    void getRoutes_Success_EmptyList() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        RouteDTO keywordDummy = RouteDTO.builder()
                .depCity("Jakarta")
                .destCity("Bandung")
                .price("150000")
                .build();

        Page<Route> emptyPage = new PageImpl<>(List.of(), pageableDummy, 0);

        when(routeRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(emptyPage);

        BasePaging<RouteDTO> result = routeService.getRoutes(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalData()).isZero();
        assertThat(result.getTotalPage()).isZero();

        verify(routeRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }
}
