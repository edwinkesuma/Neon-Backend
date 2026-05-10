package com.edwin_kesuma.Neon.domain.dtos.category;

import jakarta.validation.constraints.NotBlank;

public record RequestUpdateCategoryDTO(
        @NotBlank
        String name
) {
}
