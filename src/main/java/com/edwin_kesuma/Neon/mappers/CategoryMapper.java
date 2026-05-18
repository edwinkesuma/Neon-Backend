package com.edwin_kesuma.Neon.mappers;

import com.edwin_kesuma.Neon.domain.dtos.category.RequestCreateCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseCategoryDTO;
import com.edwin_kesuma.Neon.domain.dtos.category.ResponseSimpleCategoryDTO;
import com.edwin_kesuma.Neon.domain.entities.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category toEntity(ResponseCategoryDTO dto);

    ResponseCategoryDTO toDto(Category category);

    ResponseSimpleCategoryDTO toSimpleCategoryDto(Category category);

    Category createCategoryDtoToEntity(RequestCreateCategoryDTO createRequestCategoryDTO);

}
