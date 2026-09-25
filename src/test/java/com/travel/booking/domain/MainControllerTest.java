package com.travel.booking.domain;

import com.travel.booking.common.payload.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class MainControllerTest {

    @Test
    @DisplayName("healthCheck - Berhasil mengembalikan status sehat")
    void healthCheck_Success() {
        MainController mainController = new MainController();

        ResponseEntity<BaseResponse> response = mainController.healthCheck();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Healthy");
        assertThat(body.getData()).isNull();
        assertThat(body.getErrors()).isNull();
    }
}
