package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestUpdateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseListCategoryDTO;
import com.edwin_kesuma.Neon.domain.entities.Category;
import com.edwin_kesuma.Neon.exceptions.DuplicateResourceException;
import com.edwin_kesuma.Neon.exceptions.ResourceNotFoundException;
import com.edwin_kesuma.Neon.mappers.CategoryMapper;
import com.edwin_kesuma.Neon.repositories.CategoryRepository;
import com.edwin_kesuma.Neon.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseListCategoryDTO getAllCategories(Integer pageNumber,
                                                    Integer pageSize,
                                                    String sortBy,
                                                    String sortOrder) {
        Sort
                sortByAndOrder =
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> pageCategories = categoryRepository.findAll(pageDetails);

        List<Category> categories = pageCategories.getContent();
        List<ResponseCategoryDTO> categoryDTOS = categories.stream().map(categoryMapper::toDto).toList();

        return new ResponseListCategoryDTO(categoryDTOS,
                pageCategories.getNumber(),
                pageCategories.getSize(),
                pageCategories.getTotalElements(),
                pageCategories.getTotalPages(),
                pageCategories.isLast());
    }

    @Override
    @Transactional
    public ResponseCategoryDTO createCategory(RequestCreateCategoryDTO request) {

        String normalizedName = request.name().trim().toLowerCase();

        if (categoryRepository.existsByNormalizedCategoryName(normalizedName)) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        Category category = categoryMapper.createCategoryDtoToEntity(request);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public ResponseCategoryDTO updateCategory(RequestUpdateCategoryDTO request, UUID categoryId) {

        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        String normalizedName = request.name().trim().toLowerCase();

        if (categoryRepository.existsByNormalizedCategoryNameAndIdNot(normalizedName, categoryId)) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        category.setName(request.name());

        category.setImage(request.image() != null ? request.image() : "");

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException(
                    "Category still has products"
            );
        }

        categoryRepository.delete(category);
    }
}
