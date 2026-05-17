package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.config.AppConstans;
import com.edwin_kesuma.Neon.domain.dtos.category.*;
import com.edwin_kesuma.Neon.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseListCategoryDTO> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstans.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = AppConstans.PAGE_SIZE, required = false) Integer pageSize,
                                                                    @RequestParam(name = "sortBy", defaultValue = AppConstans.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = AppConstans.SORT_DIR, required = false) String sortOrder) {

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

    @PostMapping
    public ResponseEntity<ResponseCategoryDTO> createCategory(@Valid @RequestPart("category") RequestCreateCategoryDTO categoryDTO,
                                                              @RequestPart("image") MultipartFile image
    ) throws BadRequestException {
        ResponseCategoryDTO response = categoryService.createCategory(categoryDTO, image);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDTO> updateCategory(@Valid @RequestPart("category") RequestUpdateCategoryDTO categoryDTO,
                                                              @RequestPart("image") MultipartFile image,
                                                              @PathVariable UUID categoryId
    ) throws BadRequestException {
        ResponseCategoryDTO response = categoryService.updateCategory(categoryDTO, categoryId, image);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) throws BadRequestException {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
