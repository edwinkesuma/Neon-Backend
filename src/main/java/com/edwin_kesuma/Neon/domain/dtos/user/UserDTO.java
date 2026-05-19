package com.edwin_kesuma.Neon.domain.dtos.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        String role,
        String imageUrl) {
}
