package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.common.payload.BasePaging;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
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
    private Fleet fleetDummy;

    @BeforeEach
    void setUp() {
        requestDummy = FleetRequestDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        fleetDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();
    }

    @Test
    @DisplayName("createFleet - Berhasil menyimpan data ketika nomor kendaraan belum tersedia")
    void createFleet_Success() {
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.empty());
        when(fleetRepository.save(any(Fleet.class))).thenReturn(fleetDummy);

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

        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetDummy));
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

        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetDummy));
        when(fleetRepository.findByPlateNo(updateRequestDummy.getPlateNo())).thenReturn(Optional.of(existingFleet));
        assertThatThrownBy(() -> fleetService.updateFleet(updateRequestDummy, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Plate number of this fleet is exists");

        verify(fleetRepository, never()).save(any(Fleet.class));
    }

    @Test
    @DisplayName("deleteFleet - Berhasil menghapus data")
    void deleteFleet_Success() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetDummy));
        when(fleetRepository.existsById(1L)).thenReturn(false);

        Boolean result = fleetService.deleteFleet(1L);

        assertThat(result.booleanValue()).isTrue();

        verify(fleetRepository, times(1)).delete(any(Fleet.class));
    }

    @Test
    @DisplayName("deleteFleet - Gagal & melempar Exception ID tidak boleh kosong")
    void deleteFleet_Failed_IdIsNull() {
        assertThatThrownBy(() -> fleetService.deleteFleet(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet id cannot be null");

        verify(fleetRepository, never()).delete(any(Fleet.class));
    }

    @Test
    @DisplayName("deleteFleet - Gagal & melempar Exception data tidak tersedia")
    void deleteFleet_Failed_EntityNotFound() {
        when(fleetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fleetService.deleteFleet(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet not found");

        verify(fleetRepository, never()).delete(any(Fleet.class));
    }

    @Test
    @DisplayName("getFleetDetail - Berhasil menampilkan detail kendaraan")
    void getFleetDetail_Success() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleetDummy));

        FleetDTO result = fleetService.getFleetDetail(1L);

        assertThat(result).isNotNull();
        assertThat(result.getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getModel()).isEqualTo("Shuttle");
        assertThat(result.getTSeats()).isEqualTo(8);
    }

    @Test
    @DisplayName("getFleetDetail - Gagal & melempar Exception ID tidak boleh kosong")
    void getFleetDetail_Failed_IdIsNull() {
        assertThatThrownBy(() -> fleetService.getFleetDetail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet id cannot be null");
    }

    @Test
    @DisplayName("getFleetDetail - Gagal & melempar Exception data tidak ditemukan")
    void getFleetDetail_Failed_EntityNotFound() {
        when(fleetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fleetService.getFleetDetail(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet not found");
    }

    @Test
    @DisplayName("getFleets - Berhasil mengembalikan data berpaginasi saat data ditemukan")
    void getFleets_Success_DataExists() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        FleetDTO keywordDummy = FleetDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        List<Fleet> fleetList = List.of(fleetDummy);
        Page<Fleet> fleetPage = new PageImpl<>(fleetList, pageableDummy, 1);

        when(fleetRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(fleetPage);

        BasePaging<FleetDTO> result = fleetService.getFleets(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPage()).isEqualTo(1);
        assertThat(result.getTotalData()).isEqualTo(1L);

        FleetDTO actualDto = result.getData().getFirst();
        assertThat(actualDto.getPlateNo()).isEqualTo("D 1 AR");
        assertThat(actualDto.getModel()).isEqualTo("Shuttle");
        assertThat(actualDto.getTSeats()).isEqualTo(8);

        verify(fleetRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }

    @Test
    @DisplayName("getFleets - Berhasil mengembalikan halaman kosong saat data tidak ditemukan")
    void getFleets_Success_EmptyList() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        FleetDTO keywordDummy = FleetDTO.builder()
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        Page<Fleet> emptyPage = new PageImpl<>(List.of(), pageableDummy, 0);

        when(fleetRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(emptyPage);

        BasePaging<FleetDTO> result = fleetService.getFleets(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalData()).isZero();
        assertThat(result.getTotalPage()).isZero();

        verify(fleetRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }
}
