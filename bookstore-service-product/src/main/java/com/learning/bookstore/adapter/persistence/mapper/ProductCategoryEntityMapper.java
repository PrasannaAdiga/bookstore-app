package com.learning.bookstore.adapter.persistence.mapper;

import com.learning.bookstore.adapter.persistence.entity.ProductCategoryEntity;
import com.learning.bookstore.domain.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryEntityMapper {
    public ProductCategoryEntity convertToProductCategoryEntity(ProductCategory productCategory) {
        return ProductCategoryEntity.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .build();
    }

    public ProductCategory convertToProductCategory(ProductCategoryEntity productCategoryEntity) {
        return ProductCategory.builder()
                .id(productCategoryEntity.getId())
                .name(productCategoryEntity.getName())
                .description(productCategoryEntity.getDescription())
                .build();
    }

}
