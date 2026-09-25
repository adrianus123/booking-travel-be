package com.travel.booking.domain.auth.service.impl;

import com.travel.booking.domain.auth.dto.LoginDTO;
import com.travel.booking.domain.auth.dto.RegisterDTO;
import com.travel.booking.domain.auth.dto.UserDTO;
import com.travel.booking.domain.auth.enums.Role;
import com.travel.booking.domain.auth.jwt.JwtUtil;
import com.travel.booking.domain.auth.model.User;
import com.travel.booking.domain.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterDTO registerRequest;
    private LoginDTO loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterDTO();
        registerRequest.setEmail("user@example.com");
        registerRequest.setPassword("Password1@");

        loginRequest = new LoginDTO();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("Password1@");
    }

    @Test
    void register_Success_DefaultRoleIsUser() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserDTO result = authService.register(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(result.getRole()).isEqualTo(Role.USER.name());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-password");
        assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void register_Success_AdminRole() {
        registerRequest.setRole(Role.ADMIN.name());

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserDTO result = authService.register(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(Role.ADMIN.name());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void register_Failed_DuplicateEmail() {
        User existingUser = User.builder()
                .id(1L)
                .email(registerRequest.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn("access-token");

        Map<String, Object> result = authService.login(loginRequest);

        assertThat(result).containsEntry("accessToken", "access-token");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authenticationCaptor.capture());
        assertThat(authenticationCaptor.getValue().getPrincipal()).isEqualTo(loginRequest.getEmail());
        assertThat(authenticationCaptor.getValue().getCredentials()).isEqualTo(loginRequest.getPassword());
        verify(jwtUtil).generateToken(loginRequest.getEmail());
    }
}
