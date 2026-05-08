package com.edwin_kesuma.Neon.domain.dtos.category;

import java.util.UUID;

public record ResponseCategoryDTO(
        UUID id,
        String name,
        String image
) {
}
