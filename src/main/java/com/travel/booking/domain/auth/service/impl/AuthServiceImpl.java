package com.travel.booking.domain.auth.service.impl;

import com.travel.booking.domain.auth.dto.LoginDTO;
import com.travel.booking.domain.auth.dto.RegisterDTO;
import com.travel.booking.domain.auth.dto.UserDTO;
import com.travel.booking.domain.auth.enums.Role;
import com.travel.booking.domain.auth.jwt.JwtUtil;
import com.travel.booking.domain.auth.model.User;
import com.travel.booking.domain.auth.repository.UserRepository;
import com.travel.booking.domain.auth.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserDTO register(RegisterDTO request) {
        try {
            boolean isUserExists = userRepository.findByEmail(request.getEmail()).isPresent();

            if (isUserExists) {
                throw new IllegalArgumentException("Email already exists");
            }

            Role role = request.getRole() == null || request.getRole().isBlank()
                    ? Role.USER
                    : Role.valueOf(request.getRole());

            User newUser = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();
            userRepository.save(newUser);

            return constructUserDto(newUser);
        } catch (Exception e) {
            log.error("Failed register user: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> login(LoginDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtil.generateToken(request.getEmail());
        return Collections.singletonMap("accessToken", token);
    }

    private UserDTO constructUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
