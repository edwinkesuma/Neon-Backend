package com.edwin_kesuma.Neon.domain.dtos.user;

public record ResponseLoginDTO(
        String message,
        UserDTO user,
        String jwtToken
) {
}
