package com.travel.booking.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    @NotNull(message = "Email cannot be empty")
    @Size(min = 1, message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    private String password;
}
