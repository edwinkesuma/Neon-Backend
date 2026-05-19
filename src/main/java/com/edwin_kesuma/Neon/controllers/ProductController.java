package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.config.AppConstants;
import com.edwin_kesuma.Neon.domain.dtos.product.*;
import com.edwin_kesuma.Neon.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping
    public ResponseEntity<ResponseListProductDTO> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ResponseListProductDTO productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<ResponseListProductDTO> getProductsByCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @PathVariable UUID categoryId
    ) {
        ResponseListProductDTO
                productResponse =
                productService.getProductByCategory(pageNumber, pageSize, sortBy, sortOrder, categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("{productId}")
    public ResponseEntity<ResponseProductDetailsDTO> getProductDetails(@PathVariable UUID productId) {
        ResponseProductDetailsDTO response = productService.getProductDetails(productId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
