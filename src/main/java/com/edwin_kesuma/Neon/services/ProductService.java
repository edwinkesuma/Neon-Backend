package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.RequestUpdateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseListProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDTO;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ResponseListProductDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseProductDTO addProduct(@Valid RequestCreateProductDTO productDTO,
                                  List<MultipartFile> images) throws BadRequestException;

    ResponseProductDTO updateProduct(UUID productId,
                                     @Valid RequestUpdateProductDTO productDTO,
                                     @RequestPart("images") List<MultipartFile> images) throws BadRequestException;

    void deleteProduct(UUID productId);

    ResponseListProductDTO getProductByCategory(Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder,
                                                UUID categoryId);
}
