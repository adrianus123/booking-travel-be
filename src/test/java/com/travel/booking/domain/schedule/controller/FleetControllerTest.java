package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.domain.schedule.dto.request.FleetRequestDTO;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.service.FleetService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FleetControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private FleetController fleetController;

    @Test
    @DisplayName("createFleet - Berhasil membuat kendaraan dan meneruskan request")
    void createFleet_Success() {
        FleetRequestDTO request = FleetRequestDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        FleetDTO data = FleetDTO.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        when(fleetService.createFleet(request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = fleetController.createFleet(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Fleet created successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(fleetService, times(1)).createFleet(request);
    }

    @Test
    @DisplayName("updateFleet - Berhasil memperbarui kendaraan dan meneruskan request")
    void updateFleet_Success() {
        FleetRequestDTO request = FleetRequestDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        FleetDTO data = FleetDTO.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        when(fleetService.updateFleet(request, 1L)).thenReturn(data);

        ResponseEntity<BaseResponse> response = fleetController.updateFleet(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Fleet updated successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(fleetService, times(1)).updateFleet(request, 1L);
    }

    @Test
    @DisplayName("deleteFleet - Berhasil menghapus kendaraan dan meneruskan id")
    void deleteFleet_Success() {
        when(fleetService.deleteFleet(1L)).thenReturn(true);

        ResponseEntity<BaseResponse> response = fleetController.deleteFleet(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Fleet deleted successfully");
        assertThat(body.getData()).isEqualTo(true);
        assertThat(body.getErrors()).isNull();
        verify(fleetService, times(1)).deleteFleet(1L);
    }

    @Test
    @DisplayName("getFleets - Berhasil mengembalikan data berpaginasi")
    void getFleets_Success() {
        FleetDTO keyword = FleetDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        FleetDTO fleet = FleetDTO.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        BasePaging<FleetDTO> data = BasePaging.<FleetDTO>builder()
                .data(List.of(fleet))
                .page(0)
                .size(10)
                .totalPage(1)
                .totalData(1L)
                .build();
        when(fleetService.getFleets(keyword, pageable)).thenReturn(data);

        ResponseEntity<BaseResponse> response = fleetController.getFleets(keyword, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(fleetService, times(1)).getFleets(keyword, pageable);
    }

    @Test
    @DisplayName("getFleetDetail - Berhasil mengembalikan detail kendaraan")
    void getFleetDetail_Success() {
        FleetDTO data = FleetDTO.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
        when(fleetService.getFleetDetail(1L)).thenReturn(data);

        ResponseEntity<BaseResponse> response = fleetController.getFleetDetail(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(fleetService, times(1)).getFleetDetail(1L);
    }
}
