package com.travel.booking.common.exception;

import com.travel.booking.common.payload.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError plateNoError;

    @Mock
    private FieldError priceError;

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleValidationException - Mengembalikan seluruh error field sebagai bad request")
    void handleValidationException_ReturnsBadRequestWithFieldErrors() {
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(plateNoError, priceError));
        when(plateNoError.getField()).thenReturn("plateNo");
        when(plateNoError.getDefaultMessage()).thenReturn("Plate number is required");
        when(priceError.getField()).thenReturn("price");
        when(priceError.getDefaultMessage()).thenReturn("Price must be positive");

        ResponseEntity<BaseResponse> response = exceptionHandler.handleValidationException(validationException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).isEqualTo(Map.of(
                "plateNo", "Plate number is required",
                "price", "Price must be positive"
        ));
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("handleInvalidCredential - Mengembalikan username tidak ditemukan sebagai not found")
    void handleInvalidCredential_ReturnsNotFound() {
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");

        ResponseEntity<BaseResponse> response = exceptionHandler.handleInvalidCredential(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found");
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("handleIllegalArgumentException - Mengembalikan argument tidak valid sebagai bad request")
    void handleIllegalArgumentException_ReturnsBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Route not found");

        ResponseEntity<BaseResponse> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Route not found");
        assertThat(response.getBody().getErrors()).isNull();
        assertThat(response.getBody().getData()).isNull();
    }
}
