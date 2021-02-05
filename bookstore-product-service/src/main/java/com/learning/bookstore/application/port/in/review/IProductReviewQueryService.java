package com.learning.bookstore.application.port.in.review;

import com.learning.bookstore.application.port.in.review.response.ProductReviewResponse;

import java.util.List;

public interface IProductReviewQueryService {
    List<ProductReviewResponse> getAllReviewOfProduct(String productId, Integer page, Integer size, String sort);
    long getTotalReviewOfProduct(String productId);
}
