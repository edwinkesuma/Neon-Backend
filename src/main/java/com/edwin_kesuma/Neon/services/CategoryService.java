package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestUpdateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseListCategoryDTO;
import jakarta.validation.Valid;

import java.util.UUID;

public interface CategoryService {
    ResponseListCategoryDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseCategoryDTO createCategory(@Valid RequestCreateCategoryDTO categoryDTO);

    ResponseCategoryDTO updateCategory(@Valid RequestUpdateCategoryDTO categoryDTO, UUID categoryId);

    void deleteCategory(UUID categoryId);
}
