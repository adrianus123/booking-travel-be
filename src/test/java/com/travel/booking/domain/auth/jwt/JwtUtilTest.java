package com.travel.booking.domain.auth.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private static final String SECRET = "test-secret-key-for-jwt-unit-test-1234567890";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
    }

    @Test
    void generateToken_Success() {
        String token = jwtUtil.generateToken("user@example.com");

        assertThat(token).isNotBlank();
    }

    @Test
    void validateTokenAndRetrieveSubject_Valid() {
        String token = jwtUtil.generateToken("user@example.com");

        String email = jwtUtil.validateTokenAndRetrieveSubject(token);

        assertThat(email).isEqualTo("user@example.com");
    }

    @Test
    void validateTokenAndRetrieveSubject_Invalid() {
        assertThatThrownBy(() -> jwtUtil.validateTokenAndRetrieveSubject("invalid-token"))
                .isInstanceOf(JWTVerificationException.class);
    }
}
