package com.travel.booking.domain.auth.service;

import com.travel.booking.domain.auth.dto.LoginDTO;
import com.travel.booking.domain.auth.dto.RegisterDTO;
import com.travel.booking.domain.auth.dto.UserDTO;

import java.util.Map;

public interface AuthService {
    UserDTO register(RegisterDTO request);
    Map<String, Object> login(LoginDTO request);
}
