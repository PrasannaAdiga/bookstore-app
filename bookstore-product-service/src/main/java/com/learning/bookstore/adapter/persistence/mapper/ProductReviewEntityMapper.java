package com.learning.bookstore.adapter.persistence.mapper;

import com.learning.bookstore.adapter.persistence.entity.ProductReviewEntity;
import com.learning.bookstore.domain.ProductReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReviewEntityMapper {
    private final ProductEntityMapper productEntityMapper;

    public ProductReviewEntity convertToProductReviewEntity(ProductReview productReview) {
        return ProductReviewEntity.builder()
                .id(productReview.getId())
                .productEntity(productEntityMapper.convertToProductEntity(productReview.getProduct()))
                .userId(productReview.getUserId())
                .rating(productReview.getRating())
                .message(productReview.getMessage())
                .build();
    }

    public ProductReview convertToProductReview(ProductReviewEntity productReviewEntity) {
        return ProductReview.builder()
                .id(productReviewEntity.getId())
                .product(productEntityMapper.convertToProduct(productReviewEntity.getProductEntity()))
                .userId(productReviewEntity.getUserId())
                .rating(productReviewEntity.getRating())
                .message(productReviewEntity.getMessage())
                .build();
    }

}
