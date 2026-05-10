package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestUpdateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseListCategoryDTO;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CategoryService {
    ResponseListCategoryDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseCategoryDTO createCategory(@Valid RequestCreateCategoryDTO categoryDTO,
                                       MultipartFile image) throws BadRequestException;

    ResponseCategoryDTO updateCategory(@Valid RequestUpdateCategoryDTO categoryDTO,
                                       UUID categoryId,
                                       MultipartFile image) throws BadRequestException;

    void deleteCategory(UUID categoryId) throws BadRequestException;
}
