package com.travel.booking.domain.schedule.service.impl;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.request.ScheduleSearchRequestDTO;
import com.travel.booking.domain.schedule.dto.response.ScheduleDTO;
import com.travel.booking.domain.schedule.model.Fleet;
import com.travel.booking.domain.schedule.model.Route;
import com.travel.booking.domain.schedule.model.Schedule;
import com.travel.booking.domain.schedule.repository.FleetRepository;
import com.travel.booking.domain.schedule.repository.RouteRepository;
import com.travel.booking.domain.schedule.repository.ScheduleRepository;
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
import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.travel.booking.common.util.StringUtil.formattingPrice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private FleetRepository fleetRepository;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private ScheduleRequestDTO requestDummy;
    private Schedule scheduleDummy;
    private Fleet fleetDummy;
    private Route routeDummy;

    @BeforeEach
    void setUp() {
        requestDummy = new ScheduleRequestDTO();
        requestDummy.setPlateNo("D 1 AR");
        requestDummy.setRouteId(1L);
        requestDummy.setDepTime(OffsetDateTime.parse("2026-09-25T08:00:00+07:00"));
        requestDummy.setArrTime(OffsetDateTime.parse("2026-09-25T12:00:00+07:00"));

        fleetDummy = Fleet.builder()
                .id(1L)
                .plateNo("D 1 AR")
                .model("Shuttle")
                .tSeats(8)
                .build();

        routeDummy = Route.builder()
                .id(1L)
                .depCity("Jakarta")
                .destCity("Bandung")
                .price(new BigDecimal("150000"))
                .build();

        scheduleDummy = Schedule.builder()
                .id(1L)
                .fleet(fleetDummy)
                .route(routeDummy)
                .depTime(requestDummy.getDepTime())
                .arrTime(requestDummy.getArrTime())
                .build();
    }

    @Test
    @DisplayName("createSchedule - Berhasil menyimpan data jadwal")
    void createSchedule_Success() {
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(requestDummy.getRouteId())).thenReturn(Optional.of(routeDummy));
        when(scheduleRepository.findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                requestDummy.getDepTime(),
                requestDummy.getArrTime(),
                requestDummy.getPlateNo(),
                requestDummy.getRouteId())).thenReturn(Optional.empty());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleDummy);

        ScheduleDTO result = scheduleService.createSchedule(requestDummy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepTime()).isEqualTo(requestDummy.getDepTime());
        assertThat(result.getArrTime()).isEqualTo(requestDummy.getArrTime());
        assertThat(result.getFleet()).isNotNull();
        assertThat(result.getFleet().getId()).isEqualTo(1L);
        assertThat(result.getFleet().getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getFleet().getModel()).isEqualTo("Shuttle");
        assertThat(result.getFleet().getTSeats()).isEqualTo(8);
        assertThat(result.getRoute()).isNotNull();
        assertThat(result.getRoute().getId()).isEqualTo(1L);
        assertThat(result.getRoute().getDepCity()).isEqualTo("Jakarta");
        assertThat(result.getRoute().getDestCity()).isEqualTo("Bandung");
        assertThat(result.getRoute().getPrice()).isEqualTo(formattingPrice(new BigDecimal("150000")));

        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository, times(1)).save(scheduleCaptor.capture());
        assertThat(scheduleCaptor.getValue().getFleet()).isSameAs(fleetDummy);
        assertThat(scheduleCaptor.getValue().getRoute()).isSameAs(routeDummy);
        assertThat(scheduleCaptor.getValue().getDepTime()).isEqualTo(requestDummy.getDepTime());
        assertThat(scheduleCaptor.getValue().getArrTime()).isEqualTo(requestDummy.getArrTime());
    }

    @Test
    @DisplayName("createSchedule - Gagal & melempar Exception kendaraan tidak ditemukan")
    void createSchedule_Failed_FleetNotFound() {
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.createSchedule(requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet not found");

        verify(fleetRepository, times(1)).findByPlateNo(requestDummy.getPlateNo());
        verify(routeRepository, never()).findById(any(Long.class));
        verify(scheduleRepository, never()).findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(String.class),
                any(Long.class));
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("createSchedule - Gagal & melempar Exception rute tidak ditemukan")
    void createSchedule_Failed_RouteNotFound() {
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(requestDummy.getRouteId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.createSchedule(requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route not found");

        verify(fleetRepository, times(1)).findByPlateNo(requestDummy.getPlateNo());
        verify(routeRepository, times(1)).findById(requestDummy.getRouteId());
        verify(scheduleRepository, never()).findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(String.class),
                any(Long.class));
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("createSchedule - Gagal & melempar Exception jadwal sudah tersedia")
    void createSchedule_Failed_DuplicateSchedule() {
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(requestDummy.getRouteId())).thenReturn(Optional.of(routeDummy));
        when(scheduleRepository.findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                requestDummy.getDepTime(),
                requestDummy.getArrTime(),
                requestDummy.getPlateNo(),
                requestDummy.getRouteId())).thenReturn(Optional.of(scheduleDummy));

        assertThatThrownBy(() -> scheduleService.createSchedule(requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schedule already exists");

        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("updateSchedule - Berhasil memperbarui jadwal ketika waktu berubah")
    void updateSchedule_Success_TimeChanged() {
        ScheduleRequestDTO updateRequestDummy = new ScheduleRequestDTO();
        updateRequestDummy.setPlateNo(requestDummy.getPlateNo());
        updateRequestDummy.setRouteId(requestDummy.getRouteId());
        updateRequestDummy.setDepTime(OffsetDateTime.parse("2026-09-26T08:00:00+07:00"));
        updateRequestDummy.setArrTime(OffsetDateTime.parse("2026-09-26T12:00:00+07:00"));

        Schedule scheduleAfterSaveDummy = Schedule.builder()
                .id(1L)
                .fleet(fleetDummy)
                .route(routeDummy)
                .depTime(updateRequestDummy.getDepTime())
                .arrTime(updateRequestDummy.getArrTime())
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(fleetRepository.findByPlateNo(updateRequestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(updateRequestDummy.getRouteId())).thenReturn(Optional.of(routeDummy));
        when(scheduleRepository.findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                updateRequestDummy.getDepTime(),
                updateRequestDummy.getArrTime(),
                updateRequestDummy.getPlateNo(),
                updateRequestDummy.getRouteId())).thenReturn(Optional.empty());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleAfterSaveDummy);

        ScheduleDTO result = scheduleService.updateSchedule(1L, updateRequestDummy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepTime()).isEqualTo(updateRequestDummy.getDepTime());
        assertThat(result.getArrTime()).isEqualTo(updateRequestDummy.getArrTime());
        assertThat(result.getFleet().getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getRoute().getDestCity()).isEqualTo("Bandung");

        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).save(scheduleCaptor.capture());
        assertThat(scheduleCaptor.getValue().getDepTime()).isEqualTo(updateRequestDummy.getDepTime());
        assertThat(scheduleCaptor.getValue().getArrTime()).isEqualTo(updateRequestDummy.getArrTime());
    }

    @Test
    @DisplayName("updateSchedule - Berhasil memperbarui jadwal tanpa mengubah waktu")
    void updateSchedule_Success_TimeUnchanged() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(requestDummy.getRouteId())).thenReturn(Optional.of(routeDummy));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleDummy);

        ScheduleDTO result = scheduleService.updateSchedule(1L, requestDummy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDepTime()).isEqualTo(requestDummy.getDepTime());
        assertThat(result.getArrTime()).isEqualTo(requestDummy.getArrTime());

        verify(scheduleRepository, never()).findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(String.class),
                any(Long.class));
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    @DisplayName("updateSchedule - Gagal & melempar Exception jadwal tidak ditemukan")
    void updateSchedule_Failed_ScheduleNotFound() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.updateSchedule(99L, requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schedule not found");

        verify(scheduleRepository, never()).save(any(Schedule.class));
        verify(fleetRepository, never()).findByPlateNo(any(String.class));
        verify(routeRepository, never()).findById(any(Long.class));
    }

    @Test
    @DisplayName("updateSchedule - Gagal & melempar Exception kendaraan tidak ditemukan")
    void updateSchedule_Failed_FleetNotFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.updateSchedule(1L, requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fleet not found");

        verify(routeRepository, never()).findById(any(Long.class));
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("updateSchedule - Gagal & melempar Exception rute tidak ditemukan")
    void updateSchedule_Failed_RouteNotFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(fleetRepository.findByPlateNo(requestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(requestDummy.getRouteId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.updateSchedule(1L, requestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Route not found");

        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("updateSchedule - Gagal & melempar Exception jadwal sudah tersedia")
    void updateSchedule_Failed_DuplicateSchedule() {
        ScheduleRequestDTO updateRequestDummy = new ScheduleRequestDTO();
        updateRequestDummy.setPlateNo(requestDummy.getPlateNo());
        updateRequestDummy.setRouteId(requestDummy.getRouteId());
        updateRequestDummy.setDepTime(OffsetDateTime.parse("2026-09-26T08:00:00+07:00"));
        updateRequestDummy.setArrTime(OffsetDateTime.parse("2026-09-26T12:00:00+07:00"));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(fleetRepository.findByPlateNo(updateRequestDummy.getPlateNo())).thenReturn(Optional.of(fleetDummy));
        when(routeRepository.findById(updateRequestDummy.getRouteId())).thenReturn(Optional.of(routeDummy));
        when(scheduleRepository.findByDepTimeAndArrTimeAndFleet_PlateNoAndRoute_Id(
                updateRequestDummy.getDepTime(),
                updateRequestDummy.getArrTime(),
                updateRequestDummy.getPlateNo(),
                updateRequestDummy.getRouteId())).thenReturn(Optional.of(scheduleDummy));

        assertThatThrownBy(() -> scheduleService.updateSchedule(1L, updateRequestDummy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schedule already exists");

        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    @DisplayName("getScheduleDetail - Berhasil menampilkan detail jadwal")
    void getScheduleDetail_Success() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));

        ScheduleDTO result = scheduleService.getScheduleDetail(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFleet()).isNotNull();
        assertThat(result.getFleet().getPlateNo()).isEqualTo("D 1 AR");
        assertThat(result.getRoute()).isNotNull();
        assertThat(result.getRoute().getDepCity()).isEqualTo("Jakarta");
        assertThat(result.getDepTime()).isEqualTo(requestDummy.getDepTime());
        assertThat(result.getArrTime()).isEqualTo(requestDummy.getArrTime());
    }

    @Test
    @DisplayName("getScheduleDetail - Gagal & melempar Exception jadwal tidak ditemukan")
    void getScheduleDetail_Failed_EntityNotFound() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.getScheduleDetail(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schedule not found");
    }

    @Test
    @DisplayName("deleteSchedule - Berhasil menghapus data")
    void deleteSchedule_Success() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(scheduleRepository.existsById(1L)).thenReturn(false);

        boolean result = scheduleService.deleteSchedule(1L);

        assertThat(result).isTrue();

        verify(scheduleRepository, times(1)).delete(any(Schedule.class));
    }

    @Test
    @DisplayName("deleteSchedule - Mengembalikan false ketika data masih tersedia")
    void deleteSchedule_Success_EntityStillExists() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleDummy));
        when(scheduleRepository.existsById(1L)).thenReturn(true);

        boolean result = scheduleService.deleteSchedule(1L);

        assertThat(result).isFalse();

        verify(scheduleRepository, times(1)).delete(any(Schedule.class));
    }

    @Test
    @DisplayName("deleteSchedule - Gagal & melempar Exception jadwal tidak ditemukan")
    void deleteSchedule_Failed_EntityNotFound() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.deleteSchedule(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Schedule not found");

        verify(scheduleRepository, never()).delete(any(Schedule.class));
        verify(scheduleRepository, never()).existsById(any(Long.class));
    }

    @Test
    @DisplayName("getSchedules - Berhasil mengembalikan data berpaginasi saat data ditemukan")
    void getSchedules_Success_DataExists() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        ScheduleSearchRequestDTO keywordDummy = new ScheduleSearchRequestDTO();
        keywordDummy.setPlateNo("D 1 AR");
        keywordDummy.setModel("Shuttle");
        keywordDummy.setDepCity("Jakarta");
        keywordDummy.setDestCity("Bandung");
        keywordDummy.setDepDate(LocalDate.of(2026, Month.SEPTEMBER, 25));
        keywordDummy.setArrDate(LocalDate.of(2026, Month.SEPTEMBER, 25));

        List<Schedule> scheduleList = List.of(scheduleDummy);
        Page<Schedule> schedulePage = new PageImpl<>(scheduleList, pageableDummy, 1);

        when(scheduleRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(schedulePage);

        BasePaging<ScheduleDTO> result = scheduleService.getSchedules(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPage()).isEqualTo(1);
        assertThat(result.getTotalData()).isEqualTo(1L);

        ScheduleDTO actualDto = result.getData().getFirst();
        assertThat(actualDto.getId()).isEqualTo(1L);
        assertThat(actualDto.getFleet().getPlateNo()).isEqualTo("D 1 AR");
        assertThat(actualDto.getRoute().getDestCity()).isEqualTo("Bandung");
        assertThat(actualDto.getDepTime()).isEqualTo(requestDummy.getDepTime());
        assertThat(actualDto.getArrTime()).isEqualTo(requestDummy.getArrTime());

        verify(scheduleRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }

    @Test
    @DisplayName("getSchedules - Berhasil mengembalikan halaman kosong saat data tidak ditemukan")
    void getSchedules_Success_EmptyList() {
        Pageable pageableDummy = PageRequest.of(0, 10);

        ScheduleSearchRequestDTO keywordDummy = new ScheduleSearchRequestDTO();
        keywordDummy.setPlateNo("D 1 AR");
        keywordDummy.setModel("Shuttle");
        keywordDummy.setDepCity("Jakarta");
        keywordDummy.setDestCity("Bandung");
        keywordDummy.setDepDate(LocalDate.of(2026, Month.SEPTEMBER, 25));
        keywordDummy.setArrDate(LocalDate.of(2026, Month.SEPTEMBER, 25));

        Page<Schedule> emptyPage = new PageImpl<>(List.of(), pageableDummy, 0);

        when(scheduleRepository.findAll(any(Specification.class), eq(pageableDummy)))
                .thenReturn(emptyPage);

        BasePaging<ScheduleDTO> result = scheduleService.getSchedules(keywordDummy, pageableDummy);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalData()).isZero();
        assertThat(result.getTotalPage()).isZero();

        verify(scheduleRepository, times(1)).findAll(any(Specification.class), eq(pageableDummy));
    }
}
