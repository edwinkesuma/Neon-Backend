package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.ResponseCloudinaryUploadDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestUpdateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseListCategoryDTO;
import com.edwin_kesuma.Neon.domain.entities.Category;
import com.edwin_kesuma.Neon.exceptions.DuplicateResourceException;
import com.edwin_kesuma.Neon.exceptions.ResourceNotFoundException;
import com.edwin_kesuma.Neon.mappers.CategoryMapper;
import com.edwin_kesuma.Neon.repositories.CategoryRepository;
import com.edwin_kesuma.Neon.repositories.ProductRepository;
import com.edwin_kesuma.Neon.services.CategoryService;
import com.edwin_kesuma.Neon.services.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final CloudinaryService cloudinaryService;

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
        for (Category category : categories) {
//            System.out.println("Category Image: {}", category.getImageUrl());
            System.out.println("Category image: " + category.getImageUrl());
        }
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
    public ResponseCategoryDTO createCategory(RequestCreateCategoryDTO request,
                                              MultipartFile image) throws BadRequestException {

        String normalizedName = request.name().trim().toLowerCase();

        if (categoryRepository.existsByNormalizedCategoryName(normalizedName)) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Product image is required");
        }

        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid image file");
        }

        Category category = categoryMapper.createCategoryDtoToEntity(request);

        ResponseCloudinaryUploadDTO uploadedImage = cloudinaryService.uploadFile(image, "categories");
        category.setImageUrl(uploadedImage.imageUrl());
        category.setPublicId(uploadedImage.publicId());

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public ResponseCategoryDTO updateCategory(RequestUpdateCategoryDTO request,
                                              UUID categoryId,
                                              MultipartFile image) throws BadRequestException {

        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        String normalizedName = request.name().trim().toLowerCase();

        if (categoryRepository.existsByNormalizedCategoryNameAndIdNot(normalizedName, categoryId)) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        if (image != null && !image.isEmpty()) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new BadRequestException("Invalid image file");
            }

            if (category.getPublicId() != null) {
                cloudinaryService.deleteFile(category.getPublicId());
            }

            ResponseCloudinaryUploadDTO uploadedImage = cloudinaryService.uploadFile(image, "categories");

            category.setImageUrl(uploadedImage.imageUrl());
            category.setPublicId(uploadedImage.publicId());
        }

        category.setName(request.name().trim());
        category.setNormalizedCategoryName(normalizedName);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) throws BadRequestException {
        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (productRepository.existsByCategoryId(categoryId)) {
            throw new BadRequestException(
                    "Category still has products"
            );
        }

        if (category.getPublicId() != null) {
            cloudinaryService.deleteFile(category.getPublicId());
        }

        categoryRepository.delete(category);
    }
}
