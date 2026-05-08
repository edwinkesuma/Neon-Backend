package com.edwin_kesuma.Neon.domain.dtos.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseProductDTO(
        UUID id,
        String name,
        String image,
        Integer stock,
        BigDecimal price,
        BigDecimal discountPercentage
) {
}
