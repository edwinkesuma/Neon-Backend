package com.edwin_kesuma.Neon.repositories;

import com.edwin_kesuma.Neon.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    boolean existsByCategoryId(UUID categoryId);
}
