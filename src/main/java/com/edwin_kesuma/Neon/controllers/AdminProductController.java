package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.RequestUpdateProductDTO;
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
public class AdminProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseProductDTO> addProduct(@RequestPart("product") RequestCreateProductDTO productDTO,
                                                         @RequestPart("images") List<MultipartFile> images) throws BadRequestException {
        ResponseProductDTO addedProduct = productService.addProduct(productDTO, images);

        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@RequestPart("product") RequestUpdateProductDTO productDTO,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images,
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
