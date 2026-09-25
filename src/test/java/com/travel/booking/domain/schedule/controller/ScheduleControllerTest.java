package com.travel.booking.domain.schedule.controller;

import com.travel.booking.common.payload.BasePaging;
import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.booking.domain.schedule.dto.request.ScheduleSearchRequestDTO;
import com.travel.booking.domain.schedule.dto.response.FleetDTO;
import com.travel.booking.domain.schedule.dto.response.RouteDTO;
import com.travel.booking.domain.schedule.dto.response.ScheduleDTO;
import com.travel.booking.domain.schedule.service.ScheduleService;
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

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @Test
    @DisplayName("createSchedule - Berhasil membuat jadwal dan meneruskan request")
    void createSchedule_Success() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setPlateNo("D 1 AR");
        request.setRouteId(1L);
        request.setDepTime(OffsetDateTime.parse("2026-09-25T08:00:00+07:00"));
        request.setArrTime(OffsetDateTime.parse("2026-09-25T12:00:00+07:00"));
        ScheduleDTO data = scheduleDTO();
        when(scheduleService.createSchedule(request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = scheduleController.createSchedule(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Schedule created successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(scheduleService, times(1)).createSchedule(request);
    }

    @Test
    @DisplayName("updateSchedule - Berhasil memperbarui jadwal dan meneruskan request")
    void updateSchedule_Success() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setPlateNo("D 1 AR");
        request.setRouteId(1L);
        request.setDepTime(OffsetDateTime.parse("2026-09-25T08:00:00+07:00"));
        request.setArrTime(OffsetDateTime.parse("2026-09-25T12:00:00+07:00"));
        ScheduleDTO data = scheduleDTO();
        when(scheduleService.updateSchedule(1L, request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = scheduleController.updateSchedule(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Schedule updated successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(scheduleService, times(1)).updateSchedule(1L, request);
    }

    @Test
    @DisplayName("detailSchedule - Berhasil mengembalikan detail jadwal")
    void detailSchedule_Success() {
        ScheduleDTO data = scheduleDTO();
        when(scheduleService.getScheduleDetail(1L)).thenReturn(data);

        ResponseEntity<BaseResponse> response = scheduleController.detailSchedule(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(scheduleService, times(1)).getScheduleDetail(1L);
    }

    @Test
    @DisplayName("deleteSchedule - Berhasil menghapus jadwal dan meneruskan id")
    void deleteSchedule_Success() {
        when(scheduleService.deleteSchedule(1L)).thenReturn(true);

        ResponseEntity<BaseResponse> response = scheduleController.deleteSchedule(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Schedule deleted successfully");
        assertThat(body.getData()).isEqualTo(true);
        assertThat(body.getErrors()).isNull();
        verify(scheduleService, times(1)).deleteSchedule(1L);
    }

    @Test
    @DisplayName("getSchedules - Berhasil mengembalikan data berpaginasi")
    void getSchedules_Success() {
        ScheduleSearchRequestDTO keyword = new ScheduleSearchRequestDTO();
        keyword.setPlateNo("D 1 AR");
        keyword.setModel("Shuttle");
        keyword.setDepCity("Jakarta");
        keyword.setDestCity("Bandung");
        Pageable pageable = PageRequest.of(0, 10);
        ScheduleDTO schedule = scheduleDTO();
        BasePaging<ScheduleDTO> data = BasePaging.<ScheduleDTO>builder()
                .data(List.of(schedule))
                .page(0)
                .size(10)
                .totalPage(1)
                .totalData(1L)
                .build();
        when(scheduleService.getSchedules(keyword, pageable)).thenReturn(data);

        ResponseEntity<BaseResponse> response = scheduleController.getSchedules(keyword, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Success");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(scheduleService, times(1)).getSchedules(keyword, pageable);
    }

    private ScheduleDTO scheduleDTO() {
        return ScheduleDTO.builder()
                .id(1L)
                .fleet(FleetDTO.builder()
                        .id(1L)
                        .plateNo("D 1 AR")
                        .model("Shuttle")
                        .tSeats(8)
                        .build())
                .route(RouteDTO.builder()
                        .id(1L)
                        .depCity("Jakarta")
                        .destCity("Bandung")
                        .price("150000")
                        .build())
                .depTime(OffsetDateTime.parse("2026-09-25T08:00:00+07:00"))
                .arrTime(OffsetDateTime.parse("2026-09-25T12:00:00+07:00"))
                .build();
    }
}
