package com.travel.booking.domain.auth.controller;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import com.travel.booking.domain.auth.dto.LoginDTO;
import com.travel.booking.domain.auth.dto.RegisterDTO;
import com.travel.booking.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@Valid @RequestBody RegisterDTO request) {
        return ResponseWrapper.created("Register successfully", authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginDTO request) {
        return ResponseWrapper.ok("Login successfully", authService.login(request));
    }
}
