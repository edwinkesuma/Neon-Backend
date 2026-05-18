package com.edwin_kesuma.Neon.domain.dtos.user;

import com.edwin_kesuma.Neon.domain.entities.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestRegisterDTO(
        @NotBlank
        @Size(min = 5, max = 30, message = "The length of the name should be between 5 and 100 characters")
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20, message = "Password length must be between 8 and 50 characters")
        String password
) {
}
