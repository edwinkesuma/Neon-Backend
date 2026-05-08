package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseListProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.RequestUpdateProductDTO;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ProductService {
    ResponseListProductDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseProductDTO addProduct(@Valid RequestCreateProductDTO productDTO);

    ResponseProductDTO updateProduct(UUID productId, @Valid RequestUpdateProductDTO productDTO);

    void deleteProduct(UUID productId);

    ResponseListProductDTO getProductByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, UUID categoryId);
}
