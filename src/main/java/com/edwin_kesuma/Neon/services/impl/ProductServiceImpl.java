package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.ResponseCloudinaryUploadDTO;
import com.edwin_kesuma.Neon.domain.dtos.product.*;
import com.edwin_kesuma.Neon.domain.entities.category.Category;
import com.edwin_kesuma.Neon.domain.entities.product.Product;
import com.edwin_kesuma.Neon.domain.entities.product.ProductImage;
import com.edwin_kesuma.Neon.exceptions.DuplicateResourceException;
import com.edwin_kesuma.Neon.exceptions.ResourceNotFoundException;
import com.edwin_kesuma.Neon.mappers.ProductMapper;
import com.edwin_kesuma.Neon.repositories.CategoryRepository;
import com.edwin_kesuma.Neon.repositories.ProductRepository;
import com.edwin_kesuma.Neon.services.CloudinaryService;
import com.edwin_kesuma.Neon.services.ProductService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public ResponseListProductDTO getAllProducts(Integer pageNumber,
                                                 Integer pageSize,
                                                 String sortBy,
                                                 String sortOrder) {
        Sort
                sortByAndOrder =
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ResponseProductDTO> responseProductDTOS = products.stream().map(productMapper::productToDto).toList();

        return new ResponseListProductDTO(responseProductDTOS,
                pageProducts.getNumber(),
                pageProducts.getSize(),
                pageProducts.getTotalElements(),
                pageProducts.getTotalPages(),
                pageProducts.isLast());
    }

    @Override
    @Transactional
    public ResponseProductDTO addProduct(RequestCreateProductDTO productDTO,
                                         List<MultipartFile> images) throws BadRequestException {
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

        if (images == null || images.isEmpty()) {
            throw new BadRequestException("Product image is required");
        }


        Product product = productMapper.createProductDtoToEntity(productDTO);
        product.setCategory(selectedCategory);

        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile file : images) {

            if (!file.getContentType().startsWith("image/")) {
                throw new BadRequestException("Invalid image file");
            }

            ResponseCloudinaryUploadDTO uploadedImage = cloudinaryService.uploadFile(file, "products");

            ProductImage productImage = new ProductImage();

            productImage.setImageUrl(uploadedImage.imageUrl());
            productImage.setPublicId(uploadedImage.publicId());
            productImage.setProduct(product);

            productImages.add(productImage);
        }

        product.setImages(productImages);

        Product saved = productRepository.save(product);

        return productMapper.productToDto(saved);
    }

    @Override
    @Transactional
    public ResponseProductDTO updateProduct(UUID productId,
                                            RequestUpdateProductDTO productDTO,
                                            List<MultipartFile> images) throws BadRequestException {
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

        List<String> existingImageUrls =
                productDTO.existingImages() != null
                        ? productDTO.existingImages()
                        : new ArrayList<>();

        List<ProductImage> imagesToRemove =
                productFromDb.getImages()
                        .stream()
                        .filter(image ->
                                !existingImageUrls.contains(image.getImageUrl()))
                        .toList();

        for (ProductImage image : imagesToRemove) {
            cloudinaryService.deleteFile(image.getPublicId());
        }

        productFromDb.getImages().removeAll(imagesToRemove);

        if (images != null && !images.isEmpty()) {

            List<ProductImage> newImages = new ArrayList<>();

            for (MultipartFile file : images) {

                if (!file.getContentType().startsWith("image/")) {
                    throw new BadRequestException("Invalid image file");
                }

                ResponseCloudinaryUploadDTO uploadedImage =
                        cloudinaryService.uploadFile(file, "products");

                ProductImage productImage = new ProductImage();

                productImage.setImageUrl(uploadedImage.imageUrl());
                productImage.setPublicId(uploadedImage.publicId());
                productImage.setProduct(productFromDb);

                newImages.add(productImage);
            }

            productFromDb.getImages().addAll(newImages);
        }

        if (productFromDb.getImages().isEmpty()) {
            throw new BadRequestException("Product must have at least 1 image");
        }

        Product updatedProduct = productRepository.save(productFromDb);
        return productMapper.productToDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product
                product =
                productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        for (ProductImage oldImage : product.getImages()) {
            cloudinaryService.deleteFile(oldImage.getPublicId());
        }

        productRepository.delete(product);
    }

    @Override
    public ResponseListProductDTO getProductByCategory(Integer pageNumber,
                                                       Integer pageSize,
                                                       String sortBy,
                                                       String sortOrder,
                                                       UUID categoryId) {
        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort
                sortByAndOrder =
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable page = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategory(category, page);

        List<Product> products = pageProducts.getContent();
        List<ResponseProductDTO> responseProductDTOS = products.stream().map(productMapper::productToDto).toList();

        return new ResponseListProductDTO(responseProductDTOS,
                pageProducts.getNumber(),
                pageProducts.getSize(),
                pageProducts.getTotalElements(),
                pageProducts.getTotalPages(),
                pageProducts.isLast());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseProductDetailsDTO getProductDetails(UUID productId) {
        Product
                product =
                productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        return productMapper.productDetailsToDTO(product);
    }
}
