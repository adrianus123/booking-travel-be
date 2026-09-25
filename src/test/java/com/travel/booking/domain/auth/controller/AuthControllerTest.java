package com.travel.booking.domain.auth.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.domain.auth.dto.LoginDTO;
import com.travel.booking.domain.auth.dto.RegisterDTO;
import com.travel.booking.domain.auth.dto.UserDTO;
import com.travel.booking.domain.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("register - Berhasil mengembalikan respons dan meneruskan request")
    void register_Success() {
        RegisterDTO request = new RegisterDTO();
        request.setEmail("user@example.com");
        request.setPassword("Password1!");
        request.setRole("USER");
        UserDTO data = UserDTO.builder()
                .id(1L)
                .email("user@example.com")
                .role("USER")
                .build();
        when(authService.register(request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = authController.register(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Register successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(authService, times(1)).register(request);
    }

    @Test
    @DisplayName("login - Berhasil mengembalikan respons dan meneruskan request")
    void login_Success() {
        LoginDTO request = new LoginDTO();
        request.setEmail("user@example.com");
        request.setPassword("Password1!");
        Map<String, Object> data = Map.of("token", "jwt-token");
        when(authService.login(request)).thenReturn(data);

        ResponseEntity<BaseResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        BaseResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("Login successfully");
        assertThat(body.getData()).isSameAs(data);
        assertThat(body.getErrors()).isNull();
        verify(authService, times(1)).login(request);
    }
}
