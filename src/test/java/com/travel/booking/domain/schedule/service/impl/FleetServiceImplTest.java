package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.domain.schedule.dto.request.FleetRequestDTO;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.model.Fleet;
import com.travel.booking.domain.schedule.repository.FleetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FleetServiceImplTest {

    @Mock
    private FleetRepository fleetRepository;

    @InjectMocks
    private FleetServiceImpl fleetService;

    private FleetRequestDTO requestDummy;

    @BeforeEach
    void setUp() {
        requestDummy = FleetRequestDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
    }

    @Test
    @DisplayName("createFleet - Berhasil menyimpan data ketika nomor kendaraan belum tersedia")
    void createFleet_Success() {
        Fleet savedFleetDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.empty());
        when(fleetRepository.save(any(Fleet.class))).thenReturn(savedFleetDummy);

        FleetDTO result = fleetService.createFleet(requestDummy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getModel()).isEqualTo("Shuttle");
        assertThat(result.getTSeats()).isEqualTo(8);

        verify(fleetRepository, times(1)).findByPlateNo(requestDummy.getPlateNo());
        verify(fleetRepository, times(1)).save(any(Fleet.class));
    }

    @Test
    @DisplayName("createFleet - Gagal & melempar Exception nomor kendaraan sudah tersedia")
    void createFleet_Failed_PlateNoIsExists() {
        Fleet fleetDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));

        assertThatThrownBy(() -> fleetService.createFleet(requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Plate number of this fleet is exists");

        verify(fleetRepository, times(1)).findByPlateNo(requestDummy.getPlateNo());
        verify(fleetRepository, never()).save(any(Fleet.class));
    }

    @Test
    @DisplayName("updateFleet - Berhasil memperbarui data kendaraan tanpa mengubah nomor kendaraan")
    void updateFleet_Success_WithoutUpdatePlateNo() {
        Fleet fleetDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetDummy));
        when(fleetRepository.save(any(Fleet.class))).thenReturn(fleetDummy);

        FleetDTO result = fleetService.updateFleet(requestDummy, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getModel()).isEqualTo("Shuttle");
        assertThat(result.getTSeats()).isEqualTo(8);

        verify(fleetRepository, times(1)).findById(any(Long.class));
        verify(fleetRepository, times(1)).save(any(Fleet.class));
    }

    @Test
    @DisplayName("updateFleet - Berhasil memperbarui data kendaraan dengan mengubah nomor kendaraan")
    void updateFleet_Success_WithUpdatePlateNo() {
        Fleet fleetBeforeSaveDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        FleetRequestDTO updateRequestDummy = FleetRequestDTO.builder()
                .plateNo("D 2 TY")
                .model("Hiacce")
                .tSeats(10)
                .build();

        Fleet fleetAfterSaveDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 2 TY")
                .model("Hiacce")
                .tSeats(10)
                .build();

        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetBeforeSaveDummy));
        when(fleetRepository.findByPlateNo(updateRequestDummy.getPlateNo())).thenReturn(Optional.empty());
        when(fleetRepository.save(any(Fleet.class))).thenReturn(fleetAfterSaveDummy);

        FleetDTO result = fleetService.updateFleet(updateRequestDummy, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPlateNo()).isEqualTo("D 2 TY");
        assertThat(result.getModel()).isEqualTo("Hiacce");
        assertThat(result.getTSeats()).isEqualTo(10);

        verify(fleetRepository, times(1)).findById(any(Long.class));
        verify(fleetRepository, times(1)).findByPlateNo(updateRequestDummy.getPlateNo());
        verify(fleetRepository, times(1)).save(any(Fleet.class));
    }

    @Test
    @DisplayName("updateFleet - Gagal & melempar Exception ID tidak boleh kosong")
    void updateFleet_Failed_IdIsNull() {
        assertThatThrownBy(() -> fleetService.updateFleet(requestDummy, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet id cannot be null");

        verify(fleetRepository, never()).findById(any(Long.class));
        verify(fleetRepository, never()).findByPlateNo(any(String.class));
        verify(fleetRepository, never()).save(any(Fleet.class));
    }

    @Test
    @DisplayName("updateFleet - Gagal & melempar Exception data tidak ditemukan")
    void updateFleet_Failed_EntityNotFound() {
        when(fleetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fleetService.updateFleet(requestDummy, 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet not found");

        verify(fleetRepository, never()).findByPlateNo(any(String.class));
        verify(fleetRepository, never()).save(any(Fleet.class));
    }

    @Test
    @DisplayName("updateFleet - Gagal & melempar Exception nomor kendaraan telah tersedia")
    void updateFleet_Failed_DuplicatePlateNo() {
        Fleet fleetBeforeSaveDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        FleetRequestDTO updateRequestDummy = FleetRequestDTO.builder()
                .plateNo("D 2 TY")
                .model("Hiacce")
                .tSeats(10)
                .build();

        Fleet existingFleet = Fleet.builder()
                .id(99L)
                .plateNo("D 2 TY")
                .model("Shuttle")
                .tSeats(14)
                .build();

        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetBeforeSaveDummy));
        when(fleetRepository.findByPlateNo(updateRequestDummy.getPlateNo())).thenReturn(Optional.of(existingFleet));
        assertThatThrownBy(() -> fleetService.updateFleet(updateRequestDummy, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Plate number of this fleet is exists");

        verify(fleetRepository, never()).save(any(Fleet.class));
    }
}
