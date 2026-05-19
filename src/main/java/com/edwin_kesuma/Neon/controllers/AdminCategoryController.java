package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.RequestUpdateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseCategoryDTO> createCategory(@Valid @RequestPart("category") RequestCreateCategoryDTO categoryDTO,
                                                              @RequestPart("image") MultipartFile image
    ) throws BadRequestException {
        ResponseCategoryDTO response = categoryService.createCategory(categoryDTO, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDTO> updateCategory(@Valid @RequestPart("category") RequestUpdateCategoryDTO categoryDTO,
                                                              @RequestPart("image") MultipartFile image,
                                                              @PathVariable UUID categoryId
    ) throws BadRequestException {
        ResponseCategoryDTO response = categoryService.updateCategory(categoryDTO, categoryId, image);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) throws BadRequestException {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
