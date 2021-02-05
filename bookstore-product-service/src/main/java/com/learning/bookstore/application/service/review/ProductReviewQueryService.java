package com.learning.bookstore.application.service.review;

import com.learning.bookstore.application.port.in.review.IProductReviewQueryService;
import com.learning.bookstore.application.port.in.review.response.ProductReviewResponse;
import com.learning.bookstore.application.port.out.IProductReviewPersistenceAdapter;
import com.learning.bookstore.domain.ProductReview;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductReviewQueryService implements IProductReviewQueryService {
    private final IProductReviewPersistenceAdapter productReviewPersistenceAdapter;

    @Override
    public List<ProductReviewResponse> getAllReviewOfProduct(String productId, Integer page, Integer size, String sort) {
        List<ProductReview> productReviews = productReviewPersistenceAdapter.getAllReviewOfProductByPage(productId, page, size, sort);
        return productReviews.stream().map(new Function<ProductReview, ProductReviewResponse>() {
            @Override
            public ProductReviewResponse apply(ProductReview productReview) {
                return ProductReviewResponse.builder()
                        .productId(productReview.getProduct().getId())
                        .userId(productReview.getUserId())
                        .id(productReview.getId())
                        .rating(productReview.getRating())
                        .message(productReview.getMessage())
                        .build();
            }
        }).collect(Collectors.toList());
    }

    public long getTotalReviewOfProduct(String productId) {
        return productReviewPersistenceAdapter.getAllReviewRatingOfProduct(productId).size();
    }
}
