package com.learning.bookstore.adapter.persistence.mapper;

import com.learning.bookstore.adapter.persistence.entity.ProductCategoryEntity;
import com.learning.bookstore.adapter.persistence.entity.ProductEntity;
import com.learning.bookstore.domain.Product;
import com.learning.bookstore.domain.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {
    public ProductEntity convertToProductEntity(Product product) {
        ProductCategoryEntity productCategoryEntity = ProductCategoryEntity.builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .description(product.getCategory().getDescription())
                .build();
        return ProductEntity.builder()
                .id(product.getId() != null ? product.getId() : null)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageId(product.getImageId())
                .productCategoryEntity(productCategoryEntity)
                .availableCount(product.getAvailableCount())
                .build();
    }

    public Product convertToProduct(ProductEntity productEntity) {
        ProductCategoryEntity productCategoryEntity = productEntity.getProductCategoryEntity();
        ProductCategory productCategory = ProductCategory.builder()
                .id(productCategoryEntity.getId())
                .name(productCategoryEntity.getName())
                .description(productCategoryEntity.getDescription())
                .build();
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .imageId(productEntity.getImageId())
                .availableCount(productEntity.getAvailableCount())
                .category(productCategory)
                .build();
    }

}
