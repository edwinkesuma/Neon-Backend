package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.config.AppConstants;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseListCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseSimpleCategoryDTO;
import com.edwin_kesuma.Neon.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseListCategoryDTO> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                    @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder),
                HttpStatus.OK);
    }

    @GetMapping("/simple")
    public ResponseEntity<List<ResponseSimpleCategoryDTO>> getSimpleCategories() {
        return new ResponseEntity<>(categoryService.getSimpleCategories(), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDTO> getDetailCategory(@PathVariable UUID categoryId) {
        ResponseCategoryDTO response = categoryService.getCategory(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
