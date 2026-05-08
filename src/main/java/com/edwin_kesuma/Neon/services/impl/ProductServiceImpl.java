package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.product.*;
import com.edwin_kesuma.Neon.domain.entities.Category;
import com.edwin_kesuma.Neon.domain.entities.Product;
import com.edwin_kesuma.Neon.exceptions.DuplicateResourceException;
import com.edwin_kesuma.Neon.exceptions.ResourceNotFoundException;
import com.edwin_kesuma.Neon.mappers.ProductMapper;
import com.edwin_kesuma.Neon.repositories.CategoryRepository;
import com.edwin_kesuma.Neon.repositories.ProductRepository;
import com.edwin_kesuma.Neon.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseListProductDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort
                sortByAndOrder =
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ResponseProductDTO> responseProductDTOS = products.stream().map(productMapper::toDto).toList();

        return new ResponseListProductDTO(responseProductDTOS,
                pageProducts.getNumber(),
                pageProducts.getSize(),
                pageProducts.getTotalElements(),
                pageProducts.getTotalPages(),
                pageProducts.isLast());
    }

    @Override
    @Transactional
    public ResponseProductDTO addProduct(RequestCreateProductDTO productDTO) {
        Category
                selectedCategory =
                categoryRepository.findById(productDTO.categoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category",
                                "categoryId",
                                productDTO.categoryId()));

        boolean exists = productRepository.existsByNameAndCategoryId(
                productDTO.name(),
                productDTO.categoryId()
        );

        if (exists) {
            throw new DuplicateResourceException("Product", "name", productDTO.name());
        }

        Product product = productMapper.createProductDtoToEntity(productDTO);
        product.setImages(new ArrayList<>());
        product.setCategory(selectedCategory);

        Product saved = productRepository.save(product);

        return productMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ResponseProductDTO updateProduct(UUID productId, RequestUpdateProductDTO productDTO) {
        Product
                productFromDb =
                productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Category category = categoryRepository.findById(productDTO.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category", "categoryId", productDTO.categoryId()
                ));


        productFromDb.setName(productDTO.name());
        productFromDb.setDescription(productDTO.description());
        productFromDb.setPrice(productDTO.price());
        productFromDb.setDiscountPercentage(productDTO.discountPercentage());
        productFromDb.setStock(productDTO.stock());
        productFromDb.setCategory(category);

        Product updatedProduct = productRepository.save(productFromDb);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product
                product =
                productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(product);
    }

    @Override
    public ResponseListProductDTO getProductByCategory(Integer pageNumber,
                                                       Integer pageSize,
                                                       String sortBy,
                                                       String sortOrder,
                                                       UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable page = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(page);

        List<Product> products = pageProducts.getContent();
        List<ResponseProductDTO> responseProductDTOS = products.stream().map(productMapper::toDto).toList();

        return new ResponseListProductDTO(responseProductDTOS,
                pageProducts.getNumber(),
                pageProducts.getSize(),
                pageProducts.getTotalElements(),
                pageProducts.getTotalPages(),
                pageProducts.isLast());
    }
}
