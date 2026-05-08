package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.config.AppConstans;
import com.edwin_kesuma.Neon.domain.dtos.product.*;
import com.edwin_kesuma.Neon.services.ProductService;
import jakarta.validation.Valid;
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
            @RequestParam(name = "pageNumber", defaultValue = AppConstans.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstans.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstans.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstans.SORT_DIR, required = false) String sortOrder
    ) {
        ResponseListProductDTO productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<ResponseListProductDTO> getProductsByCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstans.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstans.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstans.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstans.SORT_DIR, required = false) String sortOrder,
            @PathVariable UUID categoryId
    ) {
        ResponseListProductDTO productResponse = productService.getProductByCategory(pageNumber, pageSize, sortBy, sortOrder, categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> addProduct(@Valid @RequestBody RequestCreateProductDTO productDTO) {
        ResponseProductDTO addedProduct = productService.addProduct(productDTO);

        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@RequestBody RequestUpdateProductDTO productDTO,
                                                            @PathVariable UUID productId) {
        ResponseProductDTO updatedProduct = productService.updateProduct(productId, productDTO);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }
}
