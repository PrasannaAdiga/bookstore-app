package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.ProductReview;

import java.util.List;

public interface IProductReviewPersistenceAdapter {
    ProductReview createOrUpdateProductReview(ProductReview productReview);
    List<ProductReview> getAllReviewOfProductByPage(String productId, Integer page, Integer size, String sort);
    List<Integer> getAllReviewRatingOfProduct(String productId);
}
