package com.edwin_kesuma.Neon.domain.dtos.category;

import java.util.List;

public record ResponseListCategoryDTO(
        List<ResponseCategoryDTO> content,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        boolean lastPage
) {
}
