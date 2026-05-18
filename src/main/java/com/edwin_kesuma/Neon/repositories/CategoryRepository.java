package com.edwin_kesuma.Neon.repositories;

import com.edwin_kesuma.Neon.domain.entities.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNormalizedCategoryName(String normalizedName);

    boolean existsByNormalizedCategoryNameAndIdNot(String normalizedName, UUID categoryId);

}
