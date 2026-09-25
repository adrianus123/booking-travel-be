package com.travel.booking.common.payload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseWrapperTest {

    @Test
    @DisplayName("created - Mengembalikan status created dengan response sukses")
    void created_ReturnsCreatedWithSuccessBody() {
        Object data = Map.of("id", 1);

        ResponseEntity<BaseResponse> response = ResponseWrapper.created("Data created", data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Data created");
        assertThat(response.getBody().getData()).isSameAs(data);
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("ok - Mengembalikan status ok dengan response sukses")
    void ok_ReturnsOkWithSuccessBody() {
        Object data = List.of("first", "second");

        ResponseEntity<BaseResponse> response = ResponseWrapper.ok("Data found", data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Data found");
        assertThat(response.getBody().getData()).isSameAs(data);
        assertThat(response.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("badRequest - Mengembalikan status bad request dengan error")
    void badRequest_ReturnsBadRequestWithErrorBody() {
        Object error = List.of("Invalid value");

        ResponseEntity<BaseResponse> response = ResponseWrapper.badRequest("Request invalid", error);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Request invalid");
        assertThat(response.getBody().getErrors()).isSameAs(error);
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("notFound - Mengembalikan status not found dengan error")
    void notFound_ReturnsNotFoundWithErrorBody() {
        Object error = List.of("Data missing");

        ResponseEntity<BaseResponse> response = ResponseWrapper.notFound("Data not found", error);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Data not found");
        assertThat(response.getBody().getErrors()).isSameAs(error);
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("internalServerError - Mengembalikan status internal server error dengan error")
    void internalServerError_ReturnsInternalServerErrorWithErrorBody() {
        Object error = Map.of("reason", "Unexpected failure");

        ResponseEntity<BaseResponse> response = ResponseWrapper.internalServerError("Server error", error);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Server error");
        assertThat(response.getBody().getErrors()).isSameAs(error);
        assertThat(response.getBody().getData()).isNull();
    }
}
