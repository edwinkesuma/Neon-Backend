package com.edwin_kesuma.Neon.domain.dtos.product;

import com.edwin_kesuma.Neon.domain.entities.ProductImage;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ResponseProductDetailsDTO(
        UUID id,
        String name,
        String description,
        List<ProductImage> images,
        Integer stock,
        BigDecimal price,
        BigDecimal discountPercentage,
        UUID categoryId
) {
}
