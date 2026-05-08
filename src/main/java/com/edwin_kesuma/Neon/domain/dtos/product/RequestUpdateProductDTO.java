package com.edwin_kesuma.Neon.domain.dtos.product;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestUpdateProductDTO(
        String name,

        String description,

        BigDecimal price,

        BigDecimal discountPercentage,

        Integer stock,

        UUID categoryId
) {
}
