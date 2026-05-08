package com.edwin_kesuma.Neon.domain.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestCreateProductDTO(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        BigDecimal price,

        BigDecimal discountPercentage,

        @NotNull
        Integer stock,

        @NotNull
        UUID categoryId
) {
}
