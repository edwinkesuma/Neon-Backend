package com.edwin_kesuma.Neon.domain.dtos.product;

import java.util.List;

public record ResponseListProductDTO(
        List<ResponseProductDTO> content,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        Boolean lastPage
) {
}
