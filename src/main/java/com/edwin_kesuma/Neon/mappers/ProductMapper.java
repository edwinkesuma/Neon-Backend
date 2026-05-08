package com.edwin_kesuma.Neon.mappers;

import com.edwin_kesuma.Neon.domain.dtos.product.RequestCreateProductDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.ResponseProductDTO;
import com.edwin_kesuma.Neon.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toEntity(ResponseProductDTO responseProductDTO);

    ResponseProductDTO toDto(Product product);

    Product createProductDtoToEntity(RequestCreateProductDTO requestCreateProductDTO);
}
