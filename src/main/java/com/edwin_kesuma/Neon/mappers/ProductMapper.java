package com.edwin_kesuma.Neon.mappers;

import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDetailsDTO;
import com.edwin_kesuma.Neon.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toEntity(ResponseProductDTO responseProductDTO);

    @Mapping(target = "image", expression = "java(getImage(product))")
    ResponseProductDTO productToDto(Product product);

    @Mapping(source = "category.id", target = "categoryId")
    ResponseProductDetailsDTO productDetailsToDTO(Product product);

    Product createProductDtoToEntity(RequestCreateProductDTO requestCreateProductDTO);

    default String getImage(Product product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }

        return product.getImages().getFirst().getImageUrl();
    }
}
