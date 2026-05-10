package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.config.AppConstans;
import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.RequestUpdateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseListProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDTO;
import com.edwin_kesuma.Neon.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
        ResponseListProductDTO
                productResponse =
                productService.getProductByCategory(pageNumber, pageSize, sortBy, sortOrder, categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseProductDTO> addProduct(@RequestPart("product") RequestCreateProductDTO productDTO,
                                                         @RequestPart("images") List<MultipartFile> images) throws BadRequestException {
        ResponseProductDTO addedProduct = productService.addProduct(productDTO, images);

        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@RequestPart("product") RequestUpdateProductDTO productDTO,
                                                            @RequestPart("images") List<MultipartFile> images,
                                                            @PathVariable UUID productId) throws BadRequestException {
        ResponseProductDTO updatedProduct = productService.updateProduct(productId, productDTO, images);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }
}
