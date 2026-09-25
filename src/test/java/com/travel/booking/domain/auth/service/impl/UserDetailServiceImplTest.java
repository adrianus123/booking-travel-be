package com.travel.booking.domain.auth.service.impl;

import com.travel.booking.domain.auth.enums.Role;
import com.travel.booking.domain.auth.model.User;
import com.travel.booking.domain.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    private User userDummy;

    @BeforeEach
    void setUp() {
        userDummy = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded-password")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void loadUserByUsername_Success() {
        when(userRepository.findByEmail(userDummy.getEmail())).thenReturn(Optional.of(userDummy));

        UserDetails result = userDetailService.loadUserByUsername(userDummy.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userDummy.getEmail());
        assertThat(result.getPassword()).isEqualTo(userDummy.getPassword());
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(Role.ADMIN.name());
        verify(userRepository).findByEmail(userDummy.getEmail());
    }

    @Test
    void loadUserByUsername_Failed_UserNotFound() {
        String username = "unknown@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with this email: " + username);

        verify(userRepository).findByEmail(username);
    }
}
